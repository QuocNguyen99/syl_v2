package com.hqnguyen.syl_v2.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hqnguyen.syl_v2.data.entity.RecordAndInfo
import com.hqnguyen.syl_v2.data.entity.RecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {

    @Query("SELECT * FROM recordentity ORDER BY id DESC")
    fun getAllRecord(): Flow<List<RecordAndInfo>>
    @Insert
    fun insertRecord(record: RecordEntity)
    @Update
    fun updateRecord(record: RecordEntity)
}