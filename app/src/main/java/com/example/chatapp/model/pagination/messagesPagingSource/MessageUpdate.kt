package com.example.chatapp.model.pagination.messagesPagingSource

import com.example.chatapp.Dtos.chat.Message

sealed class MessageUpdate {
    data class Added(val message: Message) : MessageUpdate()
    data class Updated(val message: Message) : MessageUpdate()
    data class Removed(val message: Message) : MessageUpdate()
}