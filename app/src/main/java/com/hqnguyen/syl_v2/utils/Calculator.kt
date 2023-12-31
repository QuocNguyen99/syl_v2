package com.hqnguyen.syl_v2.utils

import android.location.Location
import android.util.Log
import com.mapbox.geojson.Point
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

// v = s / t
fun calculatorSpeed(
    locationA: Point, locationB: Point, timeStart: Long, timeEnd: Long
): Double {
    val distance = calculateDistance(locationA, locationB)
    val timeHours = (timeEnd - timeStart).toDouble() / (1000.0 * 3600.0)
    Log.d("Calculator", "van toc: ${distance / timeHours}")
    val velocity = distance / timeHours
    return String.format("%.2f", velocity).toDouble()
}

//công thức Haversine
fun calculateDistance(locationA: Point, locationB: Point): Double {
    val earthRadiusKm = 6371.0 // Bán kính trái đất trong kilômét

    val dLat = Math.toRadians(locationB.latitude() - locationA.latitude())
    val dLon = Math.toRadians(locationB.longitude() - locationA.longitude())

    val lat1 = Math.toRadians(locationA.latitude())
    val lat2 = Math.toRadians(locationB.latitude())

    val a = sin(dLat / 2) * sin(dLat / 2) + sin(dLon / 2) * sin(dLon / 2) * cos(lat1) * cos(lat2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

//    val distance = earthRadiusKm * c
//    Log.d("calculateDistance", "calculateDistance: $distance")

    val locA = Location("")
    locA.latitude = locationA.latitude()
    locA.longitude = locationA.longitude()

    val locB = Location("")
    locB.latitude = locationB.latitude()
    locB.longitude = locationB.longitude()

    val distance = locA.distanceTo(locB) / 1000

    return distance.toDouble()
}

fun calculateMet(speed: Double): Int {
// Các giá trị MET khác nhau dựa trên tốc độ
    return when {
        speed < 8 -> 6 // Tốc độ chậm
        speed < 13 -> 8
        else -> 10
    }
}

fun calculateCaloriesBurned(
    weight: Double = 60.0,
    age: Int = 24,
    gender: String = "male",
    speed: Double,
    time: Double
): Float {
    val maleConstant = 0.2017
    val femaleConstant = 0.074

    val genderConstant: Double = when (gender.lowercase(Locale.getDefault())) {
        "male" -> maleConstant

        "female" -> femaleConstant
        else -> maleConstant // Mặc định sử dụng giá trị cho nam nếu giới tính không xác định
    }
    val caloriesBurnedPerMinute =
        ((genderConstant * weight) + (speed * 0.029) + (age * 0.012)) / 4.184

    return (caloriesBurnedPerMinute * time).toFloat()
}