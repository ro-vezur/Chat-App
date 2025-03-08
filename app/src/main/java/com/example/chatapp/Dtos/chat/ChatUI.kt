package com.example.chatapp.Dtos.chat

data class ChatUI(
    val id: String = "",
    val chatType: ChatType = ChatType.USER,
    val usersTyping: MutableList<String> = mutableListOf(),
    var lastMessage: String = "",
    val isPinned: Boolean = false,
    val name: String? = null,
    val imageUrl: String? = null,
)
