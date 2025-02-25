package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat

import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.others.ResourceResult

data class ChatUiState(
    val messages: List<Message> = listOf(),
    val aiAnswerResult: ResourceResult<Message> = ResourceResult.Loading(),
    val isAiGenerating: Boolean = false
)
