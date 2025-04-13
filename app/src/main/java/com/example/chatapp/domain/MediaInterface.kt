package com.example.chatapp.domain

import android.net.Uri

interface MediaInterface {
    fun getImageFromServer()
    fun uploadImageToServer(uri: Uri, onSuccess: (imageUrl: String) -> Unit)
}