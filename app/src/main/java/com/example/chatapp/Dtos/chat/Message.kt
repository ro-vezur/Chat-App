package com.example.chatapp.Dtos.chat

import com.example.chatapp.DD_MM_YYYY
import com.example.chatapp.helpers.time.getDateFromMillis

data class Message(
    val id: String = "",
    val userId: String = "",
    val chatId: String = "",
    val content: String = "",
    val sentTimeStamp: Long = 0,
    val seenBy: MutableList<String> = mutableListOf(),
    ) {
    fun formatDate(): String = getDateFromMillis(this.sentTimeStamp,DD_MM_YYYY)

}