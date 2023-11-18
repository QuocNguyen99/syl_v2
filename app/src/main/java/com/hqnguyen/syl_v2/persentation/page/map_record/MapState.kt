package com.hqnguyen.syl_v2.persentation.page.map_record

import com.hqnguyen.syl_v2.data.InfoTracking

data class MapState(
    val isShowLoading: Boolean = false,
    val timeStart: Long = System.currentTimeMillis(),
    val isRecord: Boolean = false,
    val countTime: Long = 0L,
    val infoTracking: InfoTracking = InfoTracking()
)