package com.hqnguyen.syl_v2.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
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
    private val TAG = this.javaClass.name

    @SuppressLint("MissingPermission")
    override fun getLocationUpdate(interval: Long): Flow<Location> {
        Log.e(TAG, "onLocationResult: getLocationUpdate entry")
        return callbackFlow {
            try {
                if (!context.hasLocationPermission()) {
                    Log.e(TAG, "onLocationResult: Missing permission location")
                    throw LocationAction.LocationException("Missing permission location")
                }

                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                if (!isGpsEnabled && !isNetworkEnabled) {
                    Log.e(TAG, "onLocationResult: GPS is disabled")
                    throw LocationAction.LocationException("GPS is disabled")
                }

                val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, interval).build()

                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(p0: LocationResult) {
                        super.onLocationResult(p0)
                        Log.d(TAG, "onLocationResult: location: ${p0.locations.size}")
                        Log.d(TAG, "onLocationResult: location: ${p0.locations.lastOrNull() != null}")
                        Log.d(TAG, "onLocationResult: location: ${p0.locations.lastOrNull()?.longitude} - ${p0.locations.lastOrNull()?.latitude}")
                        p0.locations.lastOrNull()?.let { location: Location ->
                            Log.d(TAG, "onLocationResult: location: ${location.longitude} - ${location.latitude}")
                            launch { send(location) }
                        }
                    }
                }

                client.requestLocationUpdates(request, locationCallback as LocationCallback, Looper.getMainLooper())

                awaitClose {
                    Log.e(TAG, "onLocationResult: remove")
                    client.removeLocationUpdates(locationCallback as LocationCallback)
                }
            } catch (ex: Exception) {
                Log.e(TAG, "onLocationResult: exception: ${ex.message}")
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