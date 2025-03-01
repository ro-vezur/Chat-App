package com.example.chatapp.helpers.time

import java.time.Instant
import java.time.temporal.ChronoUnit

fun getFutureTimeInMillis(minutesToAdd: Int): Long {
    return Instant.now().plus(minutesToAdd.toLong(),ChronoUnit.MINUTES).toEpochMilli()
}