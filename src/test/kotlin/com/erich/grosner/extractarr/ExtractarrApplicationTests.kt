package com.erich.grosner.extractarr

import com.erich.grosner.extractarr.properties.FolderConfigProperties
import org.junit.jupiter.api.Test
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@EnableConfigurationProperties(FolderConfigProperties::class)
class ExtractarrApplicationTests {

	@Test
	fun contextLoads() {
	}

}
