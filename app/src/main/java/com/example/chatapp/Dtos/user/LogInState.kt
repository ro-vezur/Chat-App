package com.example.chatapp.Dtos.user

data class LogInState(
    val failedAttempts: Int = 0,
    val blockedUntil: Long? = null,
    val lastAttemptWasAt: Long? = null,
)