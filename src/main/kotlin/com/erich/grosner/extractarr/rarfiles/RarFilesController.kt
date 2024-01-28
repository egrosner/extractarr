package com.erich.grosner.extractarr.rarfiles

import com.erich.grosner.extractarr.rarfiles.model.RarFilesResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping("/api/rarfiles")
class RarFilesController(val rarFilesRepository: RarFilesRepository) {
    @GetMapping("")
    fun get(): List<RarFilesResponse> {
        val results = rarFilesRepository.getRarFiles()
        return results
    }
}