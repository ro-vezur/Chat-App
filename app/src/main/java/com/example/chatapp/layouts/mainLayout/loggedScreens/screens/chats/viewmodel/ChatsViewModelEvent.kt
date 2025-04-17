package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chats.viewmodel

sealed class ChatsViewModelEvent {
    class NavigateTo(val route: String): ChatsViewModelEvent()
    class UpdateSearchField(val query: String) : ChatsViewModelEvent()
}