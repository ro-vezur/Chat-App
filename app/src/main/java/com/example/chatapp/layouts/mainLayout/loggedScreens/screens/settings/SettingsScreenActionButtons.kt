package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Photo
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.chatapp.Dtos.user.userSettings.SettingsSelectionValueVariants
import com.example.chatapp.navigation.ScreenRoutes

enum class SettingsScreenActionButtons(val text: String, val icon: ImageVector, val navigateRoute: Any?) {
    PRIVACY("Privacy",Icons.Filled.Lock,ScreenRoutes.LoggedScreens.SettingsGraphRoute.MainSettingsRoute.PrivacyRoute),
    NOTIFICATIONS("Notifications", Icons.Filled.Notifications,ScreenRoutes.LoggedScreens.SettingsGraphRoute.MainSettingsRoute.NotificationsRoute),
    MESSAGES("Messages",Icons.Filled.Message,ScreenRoutes.LoggedScreens.SettingsGraphRoute.MainSettingsRoute.MessagesRoute),
    MEDIA("Media",Icons.Filled.Photo,ScreenRoutes.LoggedScreens.SettingsGraphRoute.MainSettingsRoute.MediaRoute),
    APPEARANCE("Appearance",Icons.Filled.ColorLens,ScreenRoutes.LoggedScreens.SettingsGraphRoute.MainSettingsRoute.AppearanceRoute),
    LOG_OUT("Log Out",Icons.Filled.Logout,null);

    enum class SettingsPrivacyActionButtons(
        val settingName: String,
        val description: String,
        val settingType: SettingActionType,
        val selectionVariants: List<SettingsSelectionValueVariants>? = null
    ) {
        WHO_CAN_MESSAGE_ME("Who Can Message Me", "Restrict who is allowed to send you messages.",SettingActionType.SELECTION,SettingsSelectionValueVariants.usersVariants),
        WHO_CAN_SEND_FRIENDS_REQUESTS("Who Can Send Friends Requests","Manage Who Can Send You Friend Requests",SettingActionType.SELECTION,SettingsSelectionValueVariants.whoCanSendFriendRequestsVariants),
        WHO_CAN_SEE_MY_ACTIVITY("Who Can See My Activity", "Manage visibility of your online activity and status.",SettingActionType.SELECTION,SettingsSelectionValueVariants.usersVariants),
        WHO_CAN_ADD_ME_TO_GROUPS("Who Can Add Me to Groups", "Decide who can add you to group chats.",SettingActionType.SELECTION,SettingsSelectionValueVariants.usersVariants),
        WHO_CAN_SEE_MY_IMAGE("Who Can See My Image", "Control who is allowed to view your profile picture.",SettingActionType.SELECTION,SettingsSelectionValueVariants.usersVariants),
        WHO_CAN_SEE_MY_NAME("Who Can See My Name", "Choose who can see your display name.",SettingActionType.SELECTION,SettingsSelectionValueVariants.usersVariants),
    }

    enum class SettingsNotificationsActionButtons(val text: String, val icon: ImageVector) {
        ALL_NOTIFICATIONS("All Notifications",Icons.Filled.Notifications),
        ALL_CHATS("All Chats",Icons.Filled.Chat),
        ON_RECEIVING_FRIEND_REQUEST("On Receiving Friend Request",Icons.Filled.Pending),
        WHEN_SOMEBODY_DELETES_YOU("When Somebody Deletes You From Friends",Icons.Filled.Delete),
        WHEN_RECEIVER_ACCEPTS_YOUR_FRIEND_REQUEST("When Receiver Accept Your Friend Request",Icons.Filled.EmojiEmotions),
    }
}

