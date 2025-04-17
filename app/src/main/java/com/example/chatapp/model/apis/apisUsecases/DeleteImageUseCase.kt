package com.example.chatapp.model.apis.apisUsecases

import com.example.chatapp.Dtos.Media.ImageBody
import com.example.chatapp.domain.apis.MediaApiInterface
import javax.inject.Inject

class DeleteImageUseCase @Inject constructor(
    private val mediaApiInterface: MediaApiInterface
) {
    suspend operator fun invoke(image: ImageBody) {
        try {
            mediaApiInterface.deleteImage(image)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}