package com.hqnguyen.syl_v2.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val timeStart: Long,
    val countTime: Long
)