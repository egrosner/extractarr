package com.erich.grosner.extractarr

import com.erich.grosner.extractarr.properties.FolderConfigProperties
import org.junit.jupiter.api.Test
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@EnableConfigurationProperties(FolderConfigProperties::class)
@TestPropertySource(properties = arrayOf("spring.profiles.active=test"))
class ExtractarrApplicationTests {

	@Test
	fun contextLoads() {
	}

}
