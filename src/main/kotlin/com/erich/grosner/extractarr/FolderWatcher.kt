package com.erich.grosner.extractarr

import com.erich.grosner.extractarr.config.FolderConfigs
import com.erich.grosner.extractarr.properties.FolderConfigProperties
import org.jooq.DSLContext
import org.jooq.Table
import org.jooq.UpdatableRecord
import org.jooq.generated.tables.RarFiles
import org.jooq.generated.tables.records.RarFilesRecord
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.TimeUnit
import kotlin.io.path.*

@Service
class FolderWatcher(val folderToWatch: File,
                    val zipCmd: String,
                    val folderConfigProperties: FolderConfigProperties,
                    val dslContext: DSLContext) {

    val logger = LoggerFactory.getLogger(FolderWatcher::class.java)

    @Scheduled(fixedDelayString = "\${watch.time}", timeUnit = TimeUnit.MINUTES)
    fun watch() {
        logger.info("Starting scanning process...")

        //does the dir exist?
        if(!folderToWatch.exists()) {
            //the path doesn't exist
            logger.error("The folder ${folderToWatch.name} does not exist.")
            return
        }

        //is it a directory?
        if(!folderToWatch.isDirectory) {
            logger.error("The folder ${folderToWatch.name} is not a directory.")
            return
        }

        //find all the rars
        logger.info("Scanning for rars in ${folderToWatch.name}")

        //cache all the found rars and their associated db record
        val mapOfRarFilesRecords = mutableMapOf<String, RarFilesRecord>()

        val rars = Files.walk(folderToWatch.toPath()).filter { it ->

            if(it.extension != "rar" || it.isDirectory()) {
                return@filter false
            }

            //try to find one via composite key that matches to see if we processed it already
            val fileName = it.name
            val path = it.parent.absolutePathString()
            val result: RarFilesRecord? = dslContext.selectFrom(RarFiles.RAR_FILES)
                .where(RarFiles.RAR_FILES.FILE_NAME.eq(fileName))
                .and(RarFiles.RAR_FILES.PATH.eq(path))
                .fetchOne()

            //it found a record!
            return@filter result?.let {
                mapOfRarFilesRecords.put(path, result)

                if(it.status == FileStatus.SUCCESS.status) {
                    return@let false
                }
                else {
                    return@let true
                }
            } ?: true
        }.toList()

        rars.forEach {
            logger.info("File being extracted: ${it.fileName}")

            //update the status to started
            val path = it.parent.absolutePathString()
            var rarFileRecord = mapOfRarFilesRecords.getOrElse(path) {
                var newRecord = dslContext.newRecord(RarFiles.RAR_FILES).apply {
                    this.fileName = it.name
                    this.extracted = true
                    this.path = path
                    this.status = FileStatus.STARTED.status
                }
                newRecord.store()
                return@getOrElse newRecord
            }

            val result = extract(it.toFile())

            if(result) {
                //save the success
                rarFileRecord.status = FileStatus.SUCCESS.status
                rarFileRecord.update()
            }
            else {
                //it failed
                rarFileRecord.status = FileStatus.FAILURE.status
                rarFileRecord.update()
            }
        }
    }

    private fun extract(rarFile: File): Boolean {
        var pb = ProcessBuilder(zipCmd, "e", "-y", "-sdel", rarFile.absolutePath)
            .redirectErrorStream(true)
            .directory(rarFile.parentFile)
            .start()

        //handle the output
        var line: String? = pb.inputReader().readLine()
        var success = false
        while(line != null) {
            println(line)
            pb.waitFor(1L, TimeUnit.SECONDS)
            line = pb.inputReader().readLine()

            //TODO: check for a complete
            if(line?.lowercase() == "Everything is Ok".lowercase()) {
                success = true
            }

        }

        //on success we have to find the other files associated with the archive

        val acceptedExtensions = listOf("mkv", "mp4", "avi")

        if(success && folderConfigProperties.cleanup) {
            //and clean them up
            var otherFiles = rarFile.parentFile.listFiles()?.filter {
                it.nameWithoutExtension.lowercase() == rarFile.nameWithoutExtension.lowercase() &&
                        !acceptedExtensions.contains(it.extension) && it.extension.startsWith("r")
            } ?: emptyList()

            otherFiles.forEach {
                logger.info("Removing file ${it.name}")
                it.delete()
            }
        }

        return success
    }
}