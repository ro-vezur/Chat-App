package com.example.chatapp.helpers.time

import java.text.SimpleDateFormat
import java.util.Calendar

fun getDateFromMillis(millis: Long, dateFormat: String): String {
    val formatter = SimpleDateFormat(dateFormat)
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis

    return formatter.format(calendar.time)
}