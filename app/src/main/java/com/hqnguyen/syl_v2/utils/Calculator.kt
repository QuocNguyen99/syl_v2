package com.hqnguyen.syl_v2.utils

import android.util.Log
import com.mapbox.geojson.Point
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

// v = s / t
fun calculatorSpeed(
    locationA: Point,
    locationB: Point,
    timeStart: Long,
    timeEnd: Long
): Double {
    val distance = calculateDistance(locationA, locationB)
    Log.d(
        "Calculator",
        "distance: $distance time: ${((timeEnd - timeStart) / 3600)}"
    )

    val v = distance / ((timeEnd - timeStart) / 3600) // m/s
    return v
}

//công thức Haversine
fun calculateDistance(locationA: Point, locationB: Point): Double {
    val earthRadiusKm = 6371.0 // Đường kính trái đất trong kilômét

    val deltaLat = Math.toRadians(locationB.latitude() - locationA.latitude())
    val deltaLon = Math.toRadians(locationB.longitude() - locationA.longitude())

    val a = sin(deltaLat / 2) * sin(deltaLat / 2) +
            cos(Math.toRadians(locationA.latitude())) * cos(Math.toRadians(locationB.latitude())) *
            sin(deltaLon / 2) * sin(deltaLon / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return earthRadiusKm * c //km
}