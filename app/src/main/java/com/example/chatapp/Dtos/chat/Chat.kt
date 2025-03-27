package com.example.chatapp.Dtos.chat

import com.example.chatapp.Dtos.chat.chatType.ChatType

data class Chat(
    val id: String = "",
    val createdTimeStamp: Long = 0,
    val chatType: ChatType = ChatType.USER,
    val users: MutableList<String> = mutableListOf(),
    val usersTyping: MutableList<String> = mutableListOf(),
    val isPinned: Boolean = false,
    val name: String? = null,
    val imageUrl: String? = null,
    val lastMessageId: String = "",
    val lastReads: MutableMap<String,String> = mutableMapOf()
) {
    fun getOppositeUserId(currentUserId: String): String? {
        return if(chatType == ChatType.USER) users.firstOrNull { it != currentUserId } else null
    }
}