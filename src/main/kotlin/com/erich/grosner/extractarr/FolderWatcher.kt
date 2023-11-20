package com.erich.grosner.extractarr

import com.erich.grosner.extractarr.config.FolderConfigs
import com.erich.grosner.extractarr.properties.FolderConfigProperties
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
class FolderWatcher(val folderToWatch: File, val zipExe: File, val folderConfigProperties: FolderConfigProperties) {

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
        val rars = Files.walk(folderToWatch.toPath()).filter { it.extension == "rar" }.toList()

        rars.forEach {
            logger.info("File being extracted: ${it.fileName}")
            extract(it.toFile())
        }
    }

    private fun extract(rarFile: File) {
        var pb = ProcessBuilder(zipExe.absolutePath, "e", "-y", "-sdel", rarFile.absolutePath)
            .redirectErrorStream(true)
            .directory(rarFile.parentFile)
            .start()

        //handle the output
        var line: String? = pb.inputReader().readLine()
        var success: Boolean = false
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
            var otherFiles = rarFile.parentFile.listFiles().filter {
                it.nameWithoutExtension.lowercase() == rarFile.nameWithoutExtension.lowercase() &&
                        !acceptedExtensions.contains(it.extension) && it.extension.startsWith("r")
            }

            otherFiles.forEach {
                logger.info("Removing file ${it.name}")
                it.delete()
            }
        }
    }
}