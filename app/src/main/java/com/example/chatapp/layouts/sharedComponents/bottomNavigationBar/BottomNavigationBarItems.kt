package com.example.chatapp.layouts.sharedComponents.bottomNavigationBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.chatapp.navigation.ScreenRoutes

enum class BottomNavigationBarItems(val route: Any, val title: String, val icon: ImageVector,) {
    CHATS(ScreenRoutes.LoggedScreens.ChatsRoute,"Chats",Icons.Filled.Chat),
    FRIENDS(ScreenRoutes.LoggedScreens.FriendsRoute,"Friends",Icons.Filled.EmojiEmotions),
    FRIENDS_REQUESTS(ScreenRoutes.LoggedScreens.FriendRequestsRoute,"Requests",Icons.Filled.Contacts),
    SETTINGS(ScreenRoutes.LoggedScreens.SettingsGraphRoute,"Settings",Icons.Filled.Settings)
}
