package com.hqnguyen.syl_v2.data

import android.location.Location

data class Record(
    var time: Long? = null,
    var totalDistance: Double = 0.0,
    var kcal: Int = 0,
    var speed: Double = 0.0,
    val listLocation: List<Location> = listOf()
)
