package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.oneToOneChat.viewmodel

import com.example.chatapp.Dtos.chat.Chat
import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.Dtos.user.User

data class OneToOneChatUiState(
    val chat: Chat = Chat(),
    val user: User = User(),
    val unseenMessagesCount: Int = 0,
    val messageToEdit: Message? = null,
    val usersTyping: List<String> = listOf(),
)
