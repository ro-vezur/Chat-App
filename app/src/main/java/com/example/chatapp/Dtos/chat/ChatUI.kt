package com.example.chatapp.Dtos.chat

import com.example.chatapp.Dtos.chat.chatType.ChatType

data class ChatUI(
    val id: String = "",
    val chatType: ChatType = ChatType.USER,
    val usersTyping: MutableList<String> = mutableListOf(),
    var lastMessage: Message = Message(),
    val isPinned: Boolean = false,
    val name: String? = null,
    val imageUrl: String? = null,
    val userId: String? = null,
    val unseenMessagesCount: Int = 0,
)
