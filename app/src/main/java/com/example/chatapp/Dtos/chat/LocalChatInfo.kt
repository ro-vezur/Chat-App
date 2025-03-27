package com.example.chatapp.Dtos.chat

import com.example.chatapp.Dtos.chat.chatType.ChatType

data class LocalChatInfo(
    val id: String = "",
    val chatType: ChatType = ChatType.USER,
    val isPinned: Boolean = false,
)