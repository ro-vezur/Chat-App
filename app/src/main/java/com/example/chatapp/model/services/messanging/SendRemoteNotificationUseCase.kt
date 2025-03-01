package com.example.chatapp.model.services.messanging

import com.example.chatapp.Dtos.notification.SendNotificationDto
import com.example.chatapp.model.apis.fcmApi.FcmApiInterface
import com.example.chatapp.model.db.userDbUsecases.posts.fcmTokenUsecases.RemoveFcmTokenUseCase
import com.example.chatapp.others.ResourceResult
import javax.inject.Inject

class SendRemoteNotificationUseCase @Inject constructor(
    private val fcmApiInterface: FcmApiInterface,
    private val removeFcmTokenUseCase: RemoveFcmTokenUseCase
) {
    suspend operator fun invoke(sendNotificationDto: SendNotificationDto): ResourceResult<String> {
        return try {
            fcmApiInterface.sendMessage(sendNotificationDto)
            ResourceResult.Success("200")
        } catch (e: Exception) {
            removeFcmTokenUseCase(sendNotificationDto.data.receiverId,sendNotificationDto.token.toString())
            ResourceResult.Error(message = e.message.toString())
        }
    }
}