package com.erich.grosner.extractarr.rarfiles

import com.erich.grosner.extractarr.rarfiles.model.RarFilesResponse
import org.jooq.DSLContext
import org.jooq.Result
import org.jooq.generated.tables.RarFiles
import org.jooq.generated.tables.records.RarFilesRecord
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository

@Repository
class RarFilesRepository(val dslContext: DSLContext) {
    fun getRarFiles(): List<RarFilesResponse> {
        return dslContext.selectFrom(RarFiles.RAR_FILES).fetchInto(RarFilesResponse::class.java)
    }
}