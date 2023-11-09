package com.hqnguyen.syl_v2.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class InfoRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val idRecord: Long,
    val speed: Float,
    val calo: Float,
    val distance: String,
)