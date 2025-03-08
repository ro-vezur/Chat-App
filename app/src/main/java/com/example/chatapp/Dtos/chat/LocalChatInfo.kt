package com.example.chatapp.Dtos.chat

data class LocalChatInfo(
    val id: String = "",
    val chatType: ChatType = ChatType.USER,
    val isPinned: Boolean = false,
    val name: String? = null,
    val imageUrl: String? = null,
)