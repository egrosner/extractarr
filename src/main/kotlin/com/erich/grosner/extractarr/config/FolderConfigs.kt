package com.erich.grosner.extractarr.config

import com.erich.grosner.extractarr.properties.FolderConfigProperties
import com.erich.grosner.extractarr.properties.ZipConfigProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File

@Configuration
@EnableConfigurationProperties(value = [FolderConfigProperties::class, ZipConfigProperties::class])
class FolderConfigs() {
    @Bean
    fun folderToWatch(folderConfigProperties: FolderConfigProperties): File {
        return File(folderConfigProperties.folder)
    }

    @Bean
    fun zipExe(zipConfigProperties: ZipConfigProperties): File {
        return File(zipConfigProperties.exeLocation)
    }
}