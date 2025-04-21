package com.example.chatapp.Dtos.user.userSettings

data class NotificationSettings(
    val allNotifications: Boolean = true,
    val allChats: Boolean = true,
    val onReceivingFriendRequest: Boolean = true,
    val whenSomebodyDeletesYou: Boolean = true,
    val whenReceiverAcceptsYourFriendRequest: Boolean = true,
)
