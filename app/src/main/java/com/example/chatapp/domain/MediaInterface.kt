package com.example.chatapp.domain

import android.net.Uri

interface MediaInterface {
    fun getImageFromServer()
    suspend fun uploadImageToServer(uri: Uri): String
}