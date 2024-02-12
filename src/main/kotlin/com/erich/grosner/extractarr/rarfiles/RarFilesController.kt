package com.erich.grosner.extractarr.rarfiles

import com.erich.grosner.extractarr.FolderWatcher
import com.erich.grosner.extractarr.rarfiles.model.RarFilesResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping("/api/rarfiles")
class RarFilesController(val rarFilesRepository: RarFilesRepository,
                         val folderWatcher: FolderWatcher) {
    @GetMapping("")
    fun get(): List<RarFilesResponse> {
        val results = rarFilesRepository.getRarFiles()
        return results
    }

    @GetMapping("{id}/retry")
    fun retry(@PathVariable id: Int): Boolean {
        //ask folder watcher to try retrying this
        val result = folderWatcher.retry(id)
        return result
    }
}