package com.example.chatapp.domain

import com.example.chatapp.Dtos.notification.SendNotificationDto
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApiInterface {

    @POST("/send")
    suspend fun sendMessage(
        @Body body: SendNotificationDto
    )
}