package com.hqnguyen.syl_v2.persentation.page.map_record

import com.hqnguyen.syl_v2.core.BaseEffect
import com.hqnguyen.syl_v2.core.BaseEvent
import com.hqnguyen.syl_v2.core.BaseState
import com.hqnguyen.syl_v2.data.InfoTracking
import com.hqnguyen.syl_v2.data.location.MapLocation

sealed class MapEvent : BaseEvent {
    data class UpdateInfoTracking(val infoTracking: InfoTracking) : MapEvent()
    data object StartTrackingLocation : MapEvent()
    data object GetCurrentLocation : MapEvent()
    data object Start : MapEvent()
    data object Stop : MapEvent()
}

data class MapState(
    val isShowLoading: Boolean = false,
    val timeStart: Long = System.currentTimeMillis(),
    val isRecord: Boolean = false,
    val countTime: Long = 0L,
    val infoTracking: InfoTracking = InfoTracking(),
    val currentLocation: MapLocation? = MapLocation(0.0, 0.0)
) : BaseState

sealed class MapEffect : BaseEffect {
    data object NavigateToDetail : MapEffect()
}