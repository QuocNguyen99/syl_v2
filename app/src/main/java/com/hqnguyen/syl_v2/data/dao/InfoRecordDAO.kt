package com.hqnguyen.syl_v2.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.hqnguyen.syl_v2.data.entity.InfoRecordEntity

@Dao
interface InfoRecordDAO {
    @Insert
    fun insertInfoRecord(infoRecordEntity: InfoRecordEntity)
}