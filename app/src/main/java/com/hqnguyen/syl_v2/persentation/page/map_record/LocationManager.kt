package com.hqnguyen.syl_v2.persentation.page.map_record

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
import com.hqnguyen.syl_v2.utils.calculateCaloriesBurned
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.math.RoundingMode

data class LongLatData(val lng: Double = 0.0, val lat: Double = 0.0)

object LocationManager : LocationCallback() {
    private var locationClient: FusedLocationProviderClient? = null

    private val TAG = this.javaClass.name

    private var preLocation: LongLatData? = null
    private var preTime: Long = 0L

    val currentInfoTracking = MutableStateFlow(InfoTracking())

    private fun createRequest(
        timeInterval: Long,
        minimalDistance: Float
    ): LocationRequest =
        // New builder
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, timeInterval).apply {
            setMinUpdateDistanceMeters(minimalDistance)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }.build()

    @SuppressLint("MissingPermission")
    fun startLocationTracking(context: Context, timeInterval: Long, minimalDistance: Float) {
        Log.e(TAG, "startLocationTracking instance: ${this.hashCode()}")
        locationClient = LocationServices.getFusedLocationProviderClient(context)
        locationClient?.requestLocationUpdates(
            createRequest(timeInterval, minimalDistance),
            this,
            Looper.getMainLooper()
        )
    }

    fun stopLocationTracking() {
        try {
            Log.e(TAG, "stopLocationTracking instance: ${this.hashCode()}")
            val task = locationClient?.removeLocationUpdates(this)
            if (task?.isSuccessful == true) {
                Log.d(TAG, "StopLocation updates successful! ");
            } else {
                Log.d(
                    TAG,
                    "StopLocation updates unsuccessful! " + task?.exception?.stackTraceToString()
                )
            }
        } catch (ex: Exception) {
            Log.e(TAG, "stopLocationTracking: ${ex.message}")
        }
    }

    @SuppressLint("MissingPermission")
    override fun onLocationResult(location: LocationResult) {
        try {
            locationClient?.lastLocation
                ?.addOnSuccessListener { location ->
                    location?.let {
                        val lat = location.latitude
                        val long = location.longitude
                        // Update data class with location data
                        Log.d("LocationManager", "onLocationResult: ($long,$lat)")
                        preLocation?.let {
                            val speed = location.speed * 3.6 // Chuyển đổi m/s thành km/h

                            val timeDifference =
                                System.currentTimeMillis() - preTime // Thời gian tính bằng mili giây
                            val distance =
                                (speed * (timeDifference / 1000.0)) / 3600 // Kilometer
                            val kCal = calculateCaloriesBurned(
                                speed = speed,
                                time = timeDifference / 1000.0 / 60
                            )
                            Log.d(
                                "LocationManager",
                                "onLocationResult calo: $kCal speed: $speed distance ${distance.toBigDecimal()}"
                            )

                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    currentInfoTracking.emit(
                                        InfoTracking(
                                            speed = speed.toFloat(),
                                            kCal = kCal,
                                            distance = distance.toBigDecimal()
                                                .setScale(2, RoundingMode.DOWN),
                                            location = LongLatData(location.longitude, location.latitude)
                                        )
                                    )
                                } catch (ex: Exception) {
                                    Log.e("LocationManager", "${ex.message}")

                                }
                            }
                        }
                        preTime = System.currentTimeMillis()
                        preLocation = LongLatData(location.longitude, location.latitude)
                    }
                }
                ?.addOnFailureListener {
                    Log.e("LocationManager", "${it.message}")
                }
        } catch (ex: Exception) {
            Log.e("LocationManager", "${ex.message}")
        }

    }

    override fun onLocationAvailability(availability: LocationAvailability) {
        // TODO: react on the availability change
    }
}