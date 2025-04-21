package com.example.chatapp.Dtos.user

import com.example.chatapp.Dtos.chat.LocalChatInfo
import com.example.chatapp.Dtos.requests.FriendRequest
import com.example.chatapp.Dtos.user.userSettings.SettingsSelectionValueVariants
import com.example.chatapp.Dtos.user.userSettings.UserSettings
import com.example.chatapp.helpers.time.formatLastOnlineTime

data class User(
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var imageUrl: String? = null,
    var isCustomProviderUsed: Boolean = false,
    var logInState: LogInState = LogInState(),
    var localChats: MutableList<LocalChatInfo> = mutableListOf(),
    var friends: MutableList<String> = mutableListOf(),
    var requests: MutableList<FriendRequest> = mutableListOf(),
    val sentRequestsToUsers: MutableList<String> = mutableListOf(),
    var blockedUsers: MutableList<String> = mutableListOf(),
    val fcmTokens: MutableList<String> = mutableListOf(),
    val onlineStatus: UserOnlineStatus = UserOnlineStatus(),
    val settings: UserSettings = UserSettings()
) {
    fun getOppositeUserName(mainUserId: String): String {
        return when {
            settings.privacySettings.whoCanSeeMyName == SettingsSelectionValueVariants.NOBODY -> "No Name"
            settings.privacySettings.whoCanSeeMyName == SettingsSelectionValueVariants.FRIENDS && !friends.contains(mainUserId) -> "No Name"
            else -> name
        }
    }

    fun getOppositeUserImage(mainUserId: String): String? {
        return when {
            settings.privacySettings.whoCanSeeMyImage == SettingsSelectionValueVariants.NOBODY -> null
            settings.privacySettings.whoCanSeeMyName == SettingsSelectionValueVariants.FRIENDS && !friends.contains(mainUserId) -> null
            else -> imageUrl
        }
    }

    fun getOppositeUserOnlineStatus(mainUserId: String): String? {
        return when {
            settings.privacySettings.whoCanSeeMyActivity == SettingsSelectionValueVariants.NOBODY -> null
            settings.privacySettings.whoCanSeeMyActivity == SettingsSelectionValueVariants.FRIENDS && !friends.contains(mainUserId) -> null
            else -> if (onlineStatus.devices.isNotEmpty()) "Online" else  formatLastOnlineTime(onlineStatus.lastTimeSeen)
        }
    }

    fun isAllowedToMessageOppositeUser(mainUserId: String): Boolean {
        return when {
            settings.privacySettings.whoCanMessageMe == SettingsSelectionValueVariants.NOBODY -> false
            settings.privacySettings.whoCanMessageMe == SettingsSelectionValueVariants.FRIENDS && !friends.contains(mainUserId) -> false
            else -> true
        }
    }
}