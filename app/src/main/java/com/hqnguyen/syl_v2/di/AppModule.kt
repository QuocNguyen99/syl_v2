package com.hqnguyen.syl_v2.di

import android.content.Context
import androidx.room.Room
import com.hqnguyen.syl_v2.data.dao.InfoRecordDAO
import com.hqnguyen.syl_v2.data.dao.RecordDao
import com.hqnguyen.syl_v2.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "Record"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRecordDao(appDatabase: AppDatabase): RecordDao {
        return appDatabase.recordDao()
    }

    @Provides
    @Singleton
    fun provideInfoRecordDao(appDatabase: AppDatabase): InfoRecordDAO {
        return appDatabase.infoRecordDao()
    }
}