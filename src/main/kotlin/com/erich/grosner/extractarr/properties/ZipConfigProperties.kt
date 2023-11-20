package com.erich.grosner.extractarr.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix="zip")
data class ZipConfigProperties @ConstructorBinding constructor(val exeLocation: String)
