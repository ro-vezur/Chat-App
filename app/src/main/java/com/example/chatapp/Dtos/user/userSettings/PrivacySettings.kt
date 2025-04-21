package com.example.chatapp.Dtos.user.userSettings

data class PrivacySettings(
    val whoCanMessageMe: SettingsSelectionValueVariants = SettingsSelectionValueVariants.EVERYONE,
    val whoCanSendFriendsRequests: SettingsSelectionValueVariants = SettingsSelectionValueVariants.EVERYONE,
    val whoCanSeeMyActivity: SettingsSelectionValueVariants = SettingsSelectionValueVariants.EVERYONE,
    val whoCanSeeMyName: SettingsSelectionValueVariants = SettingsSelectionValueVariants.EVERYONE,
    val whoCanSeeMyImage: SettingsSelectionValueVariants = SettingsSelectionValueVariants.EVERYONE,
    val whoCanAddMeToGroups: SettingsSelectionValueVariants = SettingsSelectionValueVariants.EVERYONE,
)
