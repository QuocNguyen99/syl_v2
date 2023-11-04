package com.hqnguyen.syl_v2.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hqnguyen.syl_v2.data.dao.RecordDao
import com.hqnguyen.syl_v2.data.entity.RecordEntity

@Database(entities = [RecordEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao
}