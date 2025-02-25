package com.example.chatapp.Dtos.notification

import kotlinx.serialization.Serializable

@Serializable
data class NotificationBody(
    val title: String,
    val body: String,
)
