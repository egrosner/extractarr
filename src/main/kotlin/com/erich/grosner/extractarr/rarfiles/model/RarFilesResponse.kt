package com.erich.grosner.extractarr.rarfiles.model

data class RarFilesResponse(val id: Int, val fileName: String, val path: String, val extracted: Boolean, val status: String)
