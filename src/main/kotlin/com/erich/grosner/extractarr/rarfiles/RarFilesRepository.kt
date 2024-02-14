package com.erich.grosner.extractarr.rarfiles

import com.erich.grosner.extractarr.FileStatus
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

    fun getByid(id: Int): RarFilesRecord? {
        val result = dslContext.selectFrom(RarFiles.RAR_FILES)
                .where(RarFiles.RAR_FILES.ID.eq(id))
                .fetchOne()
        return result
    }

    fun setStatus(id: Int, status: FileStatus) {
        dslContext.update(RarFiles.RAR_FILES)
                .set(RarFiles.RAR_FILES.STATUS, status.status)
                .where(RarFiles.RAR_FILES.ID.eq(id))
                .execute()
    }
}