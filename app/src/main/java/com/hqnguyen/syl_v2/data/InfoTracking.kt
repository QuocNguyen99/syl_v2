package com.hqnguyen.syl_v2.data

import java.math.BigDecimal

data class InfoTracking(
    val speed: Float = 0f,
    val kCal: Float = 0f,
    val distance: BigDecimal = BigDecimal(0),
//    val location: LongLatData =  LongLatData()
)
