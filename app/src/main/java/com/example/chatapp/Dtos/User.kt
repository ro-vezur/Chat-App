package com.example.chatapp.Dtos

import com.example.chatapp.Dtos.requests.FriendRequest

data class User(
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var imageUrl: String? = null,
    var isCustomProviderUsed: Boolean = false,
    var chats: MutableList<String> = mutableListOf(),
    var friends: MutableList<String> = mutableListOf(),
    var requests: MutableList<FriendRequest> = mutableListOf(),
    var blockedUsers: MutableList<String> = mutableListOf(),
    val fcmTokens: MutableList<String> = mutableListOf()
)