package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chats.viewmodel

import com.example.chatapp.Dtos.chat.ChatUI
import com.example.chatapp.others.Resource

data class ChatsUiState(
    val chats: Resource<List<ChatUI>> = Resource.Loading()
)