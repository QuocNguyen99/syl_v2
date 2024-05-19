package com.hqnguyen.syl_v2.service

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationAction {
    fun getLocationUpdate(interval: Long): Flow<Location>
    suspend fun getCurrentLocation(): Location
    class LocationException(message: String) : Exception()
}