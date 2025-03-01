package com.example.chatapp.Dtos.notification

import kotlinx.serialization.Serializable

@Serializable
data class NotificationData(
    val senderId: String,
    val receiverId: String,
    val type: String,
)