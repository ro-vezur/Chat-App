package com.example.chatapp.model.db.sealedChanges

import com.example.chatapp.Dtos.chat.Message

sealed class MessageChange {
    data class Added(val message: Message) : MessageChange()
    data class Updated(val message: Message) : MessageChange()
    data class Removed(val message: Message) : MessageChange()
}