package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings

import androidx.compose.runtime.Composable
import com.example.chatapp.differentScreensSupport.checkIsPortrait
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.settingsViewModel.SettingsUiState
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.settingsViewModel.SettingsViewModelEvent
import com.example.chatapp.layouts.verticalLayout.loggedScreens.VerticalSettingsScreen

@Composable
fun SettingsScreen(
    settingsUiState: SettingsUiState,
    dispatchEvent: (SettingsViewModelEvent) -> Unit,
) {

    if(checkIsPortrait()) {
        VerticalSettingsScreen(
            settingsUiState = settingsUiState,
            dispatchEvent = dispatchEvent,
        )
    } else {

    }
}