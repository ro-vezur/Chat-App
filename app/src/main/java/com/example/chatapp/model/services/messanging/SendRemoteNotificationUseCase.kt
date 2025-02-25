package com.example.chatapp.model.services.messanging

import com.example.chatapp.Dtos.notification.SendNotificationDto
import com.example.chatapp.model.apis.fcmApi.FcmApiInterface
import com.example.chatapp.others.ResourceResult
import javax.inject.Inject

class SendRemoteNotificationUseCase @Inject constructor(
    private val fcmApiInterface: FcmApiInterface
) {
    suspend operator fun invoke(sendNotificationDto: SendNotificationDto): ResourceResult<String> {
        return try {
            fcmApiInterface.sendMessage(sendNotificationDto)
            ResourceResult.Success("200")
        } catch (e: Exception) {
            ResourceResult.Error(message = e.message.toString())
        }
    }
}