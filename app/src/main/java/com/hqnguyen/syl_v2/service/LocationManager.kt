package com.hqnguyen.syl_v2.service

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.hqnguyen.syl_v2.utils.hasLocationPermission
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

class LocationManager(private val context: Context, private val client: FusedLocationProviderClient) : LocationAction {

    @SuppressLint("MissingPermission")
    override fun getLocationUpdate(interval: Long): Flow<Location> {
        return callbackFlow {
            if (!context.hasLocationPermission()) {
                throw LocationAction.LocationException("Missing permission location")
            }

            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGpsEnabled && !isNetworkEnabled) {
                throw LocationAction.LocationException("GPS is disabled")
            }

            val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, interval).build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    p0.locations.lastOrNull()?.let { location: Location ->
                        {
                            launch { send(location) }
                        }
                    }
                }
            }

            client.requestLocationUpdates(request, locationCallback as LocationCallback, Looper.getMainLooper())

            awaitClose {
                client.removeLocationUpdates(locationCallback as LocationCallback)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @RequiresPermission("android.permission.ACCESS_COARSE_LOCATION")
    override suspend fun getCurrentLocation(): Location = suspendCancellableCoroutine { cancellableContinuation ->
        client.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    cancellableContinuation.resume(location) {}
                } ?: cancellableContinuation.resumeWithException(Exception("Fail"))
            }.addOnFailureListener { ex -> cancellableContinuation.resumeWithException(ex) }
    }
}