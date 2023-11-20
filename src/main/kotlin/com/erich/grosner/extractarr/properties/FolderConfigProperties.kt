package com.erich.grosner.extractarr.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix="watch")
data class FolderConfigProperties @ConstructorBinding constructor(val folder: String, val cleanup: Boolean, val time: String)
