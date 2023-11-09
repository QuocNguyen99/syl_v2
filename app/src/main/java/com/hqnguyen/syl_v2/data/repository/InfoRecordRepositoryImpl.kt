package com.hqnguyen.syl_v2.data.repository

import com.hqnguyen.syl_v2.data.dao.InfoRecordDAO
import com.hqnguyen.syl_v2.data.dao.RecordDao
import com.hqnguyen.syl_v2.data.entity.InfoRecordEntity
import com.hqnguyen.syl_v2.data.entity.RecordAndInfo
import com.hqnguyen.syl_v2.data.entity.RecordEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InfoRecordRepositoryImpl @Inject constructor(private val infoRecordDAO: InfoRecordDAO) {
    fun insertInfoRecord(infoRecordEntity: InfoRecordEntity) = infoRecordDAO.insertInfoRecord(infoRecordEntity)
}