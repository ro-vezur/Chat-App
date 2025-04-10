package com.example.chatapp.model.db.chatDb.observers.userChatsChanges

import com.example.chatapp.Dtos.chat.Chat

sealed class ChatChange {
    data class Added(val chat: Chat) : ChatChange()
    data class Updated(val chat: Chat) : ChatChange()
    data class Removed(val chat: Chat) : ChatChange()
}