package com.hqnguyen.syl_v2.ui.page.map_record

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationManager(
    context: Context,
    private var timeInterval: Long,
    private var minimalDistance: Float
) : LocationCallback() {
    private var request: LocationRequest
    var locationClient: FusedLocationProviderClient

    init {
        // getting the location client
        locationClient = LocationServices.getFusedLocationProviderClient(context)
        request = createRequest()
    }

    private fun createRequest(): LocationRequest =
        // New builder
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, timeInterval).apply {
            setMinUpdateDistanceMeters(minimalDistance)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }.build()

    fun changeRequest(timeInterval: Long, minimalDistance: Float) {
        this.timeInterval = timeInterval
        this.minimalDistance = minimalDistance
        createRequest()
        stopLocationTracking()
        startLocationTracking()
    }

    @SuppressLint("MissingPermission")
    fun startLocationTracking() =
        locationClient.requestLocationUpdates(request, this, Looper.getMainLooper())


    fun stopLocationTracking() {
        locationClient.flushLocations()
        locationClient.removeLocationUpdates(this)
    }

    @SuppressLint("MissingPermission")
    override fun onLocationResult(location: LocationResult) {
        locationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    val lat = location.latitude
                    val long = location.longitude
                    // Update data class with location data
                    Log.d("LocationManager", "onLocationResult: ($long,$lat)")
                }
            }
            .addOnFailureListener {
                Log.e("LocationManager", "${it.message}")
            }
    }

    override fun onLocationAvailability(availability: LocationAvailability) {
        // TODO: react on the availability change
    }
}