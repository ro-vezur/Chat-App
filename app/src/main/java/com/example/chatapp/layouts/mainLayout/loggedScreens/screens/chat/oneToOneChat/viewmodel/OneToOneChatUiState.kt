package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.oneToOneChat.viewmodel

import com.example.chatapp.Dtos.chat.Chat
import com.example.chatapp.Dtos.user.User

data class OneToOneChatUiState(
  //  val messages: MutableList<Message> = mutableListOf(),
    val chat: Chat = Chat(),
    val user: User = User(),
    val sendMessageQuery: String = "",
)
