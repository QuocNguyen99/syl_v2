package com.hqnguyen.syl_v2.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hqnguyen.syl_v2.data.entity.RecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {

    @Query("SELECT * FROM recordentity")
    fun getAllRecord(): Flow<List<RecordEntity>>

    @Insert
    fun insertRecord(record: RecordEntity)
}