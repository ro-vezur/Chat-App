package com.example.chatapp.Dtos.user

import com.example.chatapp.Dtos.chat.LocalChatInfo
import com.example.chatapp.Dtos.requests.FriendRequest

data class User(
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var imageUrl: String? = null,
    var isCustomProviderUsed: Boolean = false,
    val seenLastTimeTimeStamp: Long? = null,
    var logInState: LogInState = LogInState(),
    var localChats: MutableList<LocalChatInfo> = mutableListOf(),
    var friends: MutableList<String> = mutableListOf(),
    var requests: MutableList<FriendRequest> = mutableListOf(),
    val sentRequestsToUsers: MutableList<String> = mutableListOf(),
    var blockedUsers: MutableList<String> = mutableListOf(),
    val fcmTokens: MutableMap<String,Boolean> = mutableMapOf(),
)