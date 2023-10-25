package com.hqnguyen.syl_v2.ui.page.map_record

import com.hqnguyen.syl_v2.data.InfoTracking

data class MapState(
    var isShowLoading: Boolean = false,
    var infoTracking: InfoTracking = InfoTracking()
)