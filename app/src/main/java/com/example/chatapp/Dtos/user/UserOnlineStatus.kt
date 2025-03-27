package com.example.chatapp.Dtos.user

data class UserOnlineStatus(
    var lastTimeSeen: Long = 0,
    val devices: MutableList<String> = mutableListOf(),
)
