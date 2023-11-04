package com.hqnguyen.syl_v2.data.repository

import com.hqnguyen.syl_v2.data.dao.RecordDao
import com.hqnguyen.syl_v2.data.entity.RecordEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(private val recordDAO: RecordDao) {
    fun insertRecord(entity: RecordEntity) = recordDAO.insertRecord(entity)
    fun getAllRecord(): Flow<List<RecordEntity>> = recordDAO.getAllRecord()
}