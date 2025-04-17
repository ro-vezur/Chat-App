package com.example.chatapp.domain.apis

import com.example.chatapp.Dtos.Media.ImageBody
import retrofit2.http.Body
import retrofit2.http.POST

interface MediaApiInterface {
    @POST("/deleteImage")
    suspend fun deleteImage(
        @Body body: ImageBody
    )
}