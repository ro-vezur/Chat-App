package com.example.chatapp.Dtos.contact

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val id: String,
    val name: String,
    val image: String,
    val lastMessage: String,
    val isPinned: Boolean,
    val chatType: ContactsTypes
)