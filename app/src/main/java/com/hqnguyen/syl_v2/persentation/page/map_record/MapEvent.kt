package com.hqnguyen.syl_v2.persentation.page.map_record

import com.hqnguyen.syl_v2.data.InfoTracking

sealed class MapEvent {
    data class UpdateInfoTracking(val infoTracking: InfoTracking) : MapEvent()
    object GetCurrentLocation : MapEvent()
    object Start : MapEvent()
    object Stop : MapEvent()

}