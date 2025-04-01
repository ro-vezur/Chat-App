package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.oneToOneChat.viewmodel

import com.example.chatapp.Dtos.chat.LocalChatInfo
import com.example.chatapp.Dtos.chat.Message

sealed class OneToOneChatViewModelEvent {
    class AddLocalChatInfo(val userId: String,val localChatInfo: LocalChatInfo): OneToOneChatViewModelEvent()
    class SendMessage(val message: Message): OneToOneChatViewModelEvent()
    class OnEnterQueryChange(val query: String): OneToOneChatViewModelEvent()
    class AddMessageToReadList(val message: Message, val userId: String): OneToOneChatViewModelEvent()
    data object ClearMessagesReadList: OneToOneChatViewModelEvent()
    class SetMessagesReadStatus(val userId: String): OneToOneChatViewModelEvent()
    class SetUnseenMessagesCount(val userId: String): OneToOneChatViewModelEvent()
    class AddUserTyping(val chatId: String,val userId: String): OneToOneChatViewModelEvent()
    class RemoveUserTyping(val chatId: String,val userId: String): OneToOneChatViewModelEvent()
}