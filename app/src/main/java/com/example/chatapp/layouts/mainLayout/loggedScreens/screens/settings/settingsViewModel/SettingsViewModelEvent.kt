package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.settingsViewModel

import com.example.chatapp.Dtos.user.userSettings.UserSettings

sealed class SettingsViewModelEvent {
    data object LogOut : SettingsViewModelEvent()
    class UpdateUserSettings(val userId: String,val settings: UserSettings): SettingsViewModelEvent()
}