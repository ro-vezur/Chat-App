package com.example.chatapp.Dtos.chat

import kotlinx.serialization.Serializable

@Serializable
data class Message(
  //  val id: String = "",
    val role: String = "",
    val content: String = "",
   // val sentDate: String = "",
)