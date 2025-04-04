package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chats.viewmodel

import com.example.chatapp.Dtos.chat.LocalChatInfo

sealed class ChatsViewModelEvent {
    class NavigateTo(val route: String): ChatsViewModelEvent()
    class FetchUserChats(val localChats: List<LocalChatInfo>): ChatsViewModelEvent()
}