package com.example.chatapp.Dtos.chat

import kotlinx.serialization.Serializable

@Serializable
data class Chat(
    val id: String,
    val messages: List<String>,
    val createdDate: String,
    val lastMessageDate: String
)