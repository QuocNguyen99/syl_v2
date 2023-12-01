package com.hqnguyen.syl_v2.service

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    fun getLocationUpdate(interval: Long): Flow<Location>

    class LocationException(message: String) : Exception()
}