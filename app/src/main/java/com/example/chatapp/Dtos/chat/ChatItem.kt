package com.example.chatapp.Dtos.chat

sealed class ChatItem {
    data class DateHeader(val date: String): ChatItem()
    data class MessageItem(val message: Message): ChatItem()
    data class NewMessagesSeparator(val text: String = "New Messages"): ChatItem()
}