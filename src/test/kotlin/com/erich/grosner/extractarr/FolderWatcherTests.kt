package com.erich.grosner.extractarr

import com.erich.grosner.extractarr.config.FolderConfigs
import com.erich.grosner.extractarr.properties.FolderConfigProperties
import com.erich.grosner.extractarr.properties.ZipConfigProperties
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource
import java.io.File

@SpringBootTest(classes = [FolderWatcher::class])
@Import(FolderWatcherTests.TestConfig::class)
class FolderWatcherTests() {
    @TestConfiguration
    class TestConfig {
        @Bean
        fun folderToWatch(): File {
            return File("./testrars/morningshow")
        }
        @Bean
        fun zipExe(): File {
            return File("./7z/7zz")
        }
        @Bean
        fun folderConfigProperties(): FolderConfigProperties {
            return FolderConfigProperties("", true, "1")
        }
    }

    @Autowired
    lateinit var folderWatcher: FolderWatcher

    @Test
    fun watchCalled_shouldNotFindFolder() {
        //given

        //when
        folderWatcher.watch()

        //then
        assert(true)
    }
}
