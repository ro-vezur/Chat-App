package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chats

import com.example.chatapp.Dtos.contact.Contact
import com.example.chatapp.fakeContacts

data class ChatsUiState(
    val contacts: List<Contact> = fakeContacts
)