package com.example.chatapp.Dtos.chat

import com.example.chatapp.Dtos.chat.chatType.ChatType
import com.example.chatapp.Dtos.user.User

data class ChatUI(
    val id: String = "",
    val chatType: ChatType = ChatType.USER,
    var lastMessage: Message = Message(),
    val isPinned: Boolean = false,
    val name: String? = null,
    val imageUrl: String? = null,
    val user: User? = null,
    val unseenMessagesCount: Int = 0,
    val typingUsersText: String? = null,
    val lastUpdateTimestamp: Long = 0,
)
