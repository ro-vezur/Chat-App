package com.example.chatapp.layouts.verticalLayout.loggedScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.chatapp.differentScreensSupport.sdp
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.settingsViewModel.SettingsUiState
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.settingsViewModel.SettingsViewModelEvent

@Composable
fun VerticalSettingsScreen(
    settingsUiState: SettingsUiState,
    dispatchEvent: (SettingsViewModelEvent) -> Unit,
) {

    Column(
        modifier = Modifier
            .padding(top = 12.sdp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(onClick = {
            dispatchEvent(SettingsViewModelEvent.LogOut)
        }) {
            Text(text = "Log Out")
        }
    }

}