package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.oneToOneChat.viewmodel

import com.example.chatapp.Dtos.chat.LocalChatInfo
import com.example.chatapp.Dtos.chat.Message

sealed class OneToOneChatViewModelEvent {
    class AddLocalChatInfo(val userId: String,val localChatInfo: LocalChatInfo): OneToOneChatViewModelEvent()
    class SendMessage(val message: Message): OneToOneChatViewModelEvent()
    class OnEnterQueryChange(val query: String): OneToOneChatViewModelEvent()
}