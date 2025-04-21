package com.example.chatapp.Dtos.user.userSettings

data class UserSettings(
    val notificationSettings: NotificationSettings = NotificationSettings(),
    val privacySettings: PrivacySettings = PrivacySettings(),
    val theme: SettingsSelectionValueVariants = SettingsSelectionValueVariants.SYSTEM,
)
