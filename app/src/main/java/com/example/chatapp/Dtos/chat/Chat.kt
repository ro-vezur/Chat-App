package com.example.chatapp.Dtos.chat

data class Chat(
    val id: String = "",
    val createdTimeStamp: Long = 0,
    val chatType: ChatType = ChatType.USER,
    val users: MutableList<String> = mutableListOf(),
    val usersTyping: MutableList<String> = mutableListOf(),
    val messages: MutableList<String> = mutableListOf(),
    val isPinned: Boolean = false,
    val name: String? = null,
    val imageUrl: String? = null,
)