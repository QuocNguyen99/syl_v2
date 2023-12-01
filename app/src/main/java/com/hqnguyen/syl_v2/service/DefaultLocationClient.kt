package com.hqnguyen.syl_v2.service

import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DefaultLocationClient(private val context:Context,private val client: FusedLocationProviderClient): LocationClient {
    override fun getLocationUpdate(interval: Long): Flow<Location> {
        return callbackFlow {

        }
    }
}