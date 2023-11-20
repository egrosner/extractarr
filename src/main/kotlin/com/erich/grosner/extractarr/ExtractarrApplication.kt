package com.erich.grosner.extractarr

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class ExtractarrApplication

fun main(args: Array<String>) {
	runApplication<ExtractarrApplication>(*args)
}
