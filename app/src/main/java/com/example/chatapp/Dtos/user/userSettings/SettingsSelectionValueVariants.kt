package com.example.chatapp.Dtos.user.userSettings

enum class SettingsSelectionValueVariants(val text: String) {
    EVERYONE("Everyone"),
    FRIENDS("Friends"),
    NOBODY("Nobody"),
    LIGHT("Light"),
    DARK("Dark"),
    SYSTEM("System");

    companion object {
        val themeVariants = listOf(LIGHT,DARK,SYSTEM)
        val usersVariants = listOf(EVERYONE,FRIENDS,NOBODY)
        val whoCanSendFriendRequestsVariants = listOf(EVERYONE,NOBODY)
    }
}