package com.example.chatapp.helpers.time

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun formatLastOnlineTime(timeMillis: Long): String {
    val calendar = Calendar.getInstance().apply { timeInMillis = timeMillis }

    val dateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val dateFormatFull = SimpleDateFormat("MMMM dd, yyyy 'at' h:mm a", Locale.getDefault())

    val todayStart = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    val yesterdayStart = todayStart - 24 * 60 * 60 * 1000

    return when {
        timeMillis >= todayStart -> "Today at ${dateFormat.format(calendar.time)}"
        timeMillis >= yesterdayStart -> "Yesterday at ${dateFormat.format(calendar.time)}"
        else -> dateFormatFull.format(calendar.time)
    }
}