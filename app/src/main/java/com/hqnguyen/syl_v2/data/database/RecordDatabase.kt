package com.hqnguyen.syl_v2.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hqnguyen.syl_v2.data.dao.InfoRecordDAO
import com.hqnguyen.syl_v2.data.dao.RecordDao
import com.hqnguyen.syl_v2.data.entity.InfoRecordEntity
import com.hqnguyen.syl_v2.data.entity.RecordEntity

@Database(
    entities = [RecordEntity::class, InfoRecordEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao
    abstract fun infoRecordDao(): InfoRecordDAO
}