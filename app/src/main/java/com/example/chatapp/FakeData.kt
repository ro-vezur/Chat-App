package com.example.chatapp

import com.example.chatapp.Dtos.contact.Contact
import com.example.chatapp.Dtos.contact.ContactsTypes

val fakeContacts = listOf(
    Contact(
        id = "1",
        name = "Osama Bin Laden",
        image = "https://upload.wikimedia.org/wikipedia/commons/c/ca/Osama_bin_Laden_portrait.jpg",
        lastMessage = "Happy 9/11!",
        isPinned = false,
        chatType = ContactsTypes.USER
    ),
    Contact(
        id = "2",
        name = "Osama Bin Laden",
        image = "https://upload.wikimedia.org/wikipedia/commons/c/ca/Osama_bin_Laden_portrait.jpg",
        lastMessage = "Happy 9/11!",
        isPinned = false,
        chatType = ContactsTypes.USER
    ),
    Contact(
        id = "3",
        name = "Osama Bin Laden",
        image = "https://upload.wikimedia.org/wikipedia/commons/c/ca/Osama_bin_Laden_portrait.jpg",
        lastMessage = "Happy 9/11!",
        isPinned = false,
        chatType = ContactsTypes.USER
    )
)