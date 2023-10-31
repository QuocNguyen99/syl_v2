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
import com.hqnguyen.syl_v2.data.InfoTracking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LongLatData(val lng: Double, val lat: Double)

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

    private var preLocation: LongLatData? = null
    private var preTime: Long = 0L

    private val _currentInfoTracking = MutableStateFlow(InfoTracking())
    val currentInfoTracking: StateFlow<InfoTracking> = _currentInfoTracking

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
                    preLocation?.let {
                        val speed = location.speed * 3.6 // Chuyển đổi m/s thành km/h

                        val timeDifference = System.currentTimeMillis() - preTime // Thời gian tính bằng mili giây
                        val distance = (speed * (timeDifference / 1000)) / 3600 // Kilometer
                        Log.d(TAG, "onLocationResult: ${distance.toBigDecimal()}")
                        preTime = System.currentTimeMillis()
                        val kCal = 1f

                        CoroutineScope(Dispatchers.IO).launch {
                            _currentInfoTracking.emit(
                                InfoTracking(
                                    speed = speed.toFloat(),
                                    kCal = kCal,
                                    distance = distance.toBigDecimal()
                                )
                            )
                        }
                    }

                    preLocation = LongLatData(location.longitude, location.latitude)
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