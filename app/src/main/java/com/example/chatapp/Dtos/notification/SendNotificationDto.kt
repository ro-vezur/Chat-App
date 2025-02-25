package com.example.chatapp.Dtos.notification

import kotlinx.serialization.Serializable

@Serializable
data class SendNotificationDto(
    val token: String?,
    val topic: String?,
    val priority: String = "high",
    val notificationBody: NotificationBody,
    val data: NotificationData,
)