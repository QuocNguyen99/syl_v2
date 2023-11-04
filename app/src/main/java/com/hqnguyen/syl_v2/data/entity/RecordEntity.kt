package com.hqnguyen.syl_v2.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val speed: Float,
    val calo: Float,
    val distance: String,
    val timeStart: Long )