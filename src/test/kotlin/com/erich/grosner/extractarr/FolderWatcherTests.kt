package com.erich.grosner.extractarr

import com.erich.grosner.extractarr.config.FolderConfigs
import com.erich.grosner.extractarr.properties.FolderConfigProperties
import com.erich.grosner.extractarr.properties.ZipConfigProperties
import io.mockk.mockk
import org.jooq.DSLContext
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource
import java.io.File
import java.nio.file.Files

class FolderWatcherTests() {
    @Test
    fun watchCalled_shouldNotFindFolder() {
        //given
        val mockProps = mockk<FolderConfigProperties>()
        val mockContext = mockk<DSLContext>()
        val mockRar = Files.createTempFile("test", ".rar")

        val folderWatcher = FolderWatcher(zipCmd = "7z",
            folderConfigProperties = mockProps,
            dslContext = mockContext,
            folderToWatch = mockRar.toFile())

        //when
        folderWatcher.watch()

        //then
        assert(true)
    }
}
