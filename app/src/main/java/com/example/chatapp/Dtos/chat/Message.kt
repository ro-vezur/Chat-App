package com.example.chatapp.Dtos.chat

import com.example.chatapp.DD_MM_YYYY
import com.example.chatapp.Dtos.chat.enums.MessageType
import com.example.chatapp.helpers.time.getDateFromMillis

data class Message(
    val id: String = "",
    val userId: String = "",
    val chatId: String = "",
    val content: String = "",
    val messageType: MessageType = MessageType.TEXT,
    val sentTimeStamp: Long? = null,
    val edited: Boolean = false,
    val seenBy: MutableMap<String,Long> = mutableMapOf(),
    ) {
    fun formatDate(): String = sentTimeStamp?.let { getDateFromMillis(this.sentTimeStamp,DD_MM_YYYY) } ?: ""

}