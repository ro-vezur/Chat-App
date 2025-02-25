package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.settingsViewModel

sealed class SettingsViewModelEvent {
    data object LogOut : SettingsViewModelEvent()
}