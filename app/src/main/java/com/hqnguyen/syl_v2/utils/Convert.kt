package com.hqnguyen.syl_v2.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun Long.toTimeWithFormat(format: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")): String {
    val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
    return localDateTime.format(format)
}

@RequiresApi(Build.VERSION_CODES.O)
fun Long.secondToDayHour(): String {
    val duration = Duration.ofSeconds(this)

    // Extract hours and minutes from the Duration
    val hours = duration.toHours()
    val minutes = duration.minusHours(hours).toMinutes()

    // Format hours and minutes as "hh:mm"
    return String.format("%02d:%02d", hours, minutes)
}