package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.LocalUser
import com.example.chatapp.differentScreensSupport.sdp
import com.example.chatapp.helpers.navigation.singleClickNavigate
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.settingsViewModel.SettingsUiState
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.settingsViewModel.SettingsViewModelEvent
import com.example.chatapp.layouts.sharedComponents.images.UserImage
import com.example.chatapp.navigation.ScreenRoutes

@Composable
fun SettingsScreen(
    navController: NavController,
    settingsUiState: SettingsUiState,
    dispatchEvent: (SettingsViewModelEvent) -> Unit,
) {
    val user = LocalUser.current

    Column(
        modifier = Modifier
            .padding(top = 12.sdp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier
                .padding(vertical = 20.sdp)
                .clickable {
                    navController.singleClickNavigate(ScreenRoutes.LoggedScreens.SettingsGraphRoute.EditProfileRoute)
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.sdp)
        ) {
            UserImage(
                modifier = Modifier
                    .size(100.sdp)
                    .clip(CircleShape),
                imageUrl = user.imageUrl
            )

            Text(
                modifier = Modifier
                    .padding(top = 8.sdp),
                text = user.name,
                style = MaterialTheme.typography.displaySmall,
            )

            Text(
                modifier = Modifier,
                text = user.email,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

        TextButton(
            modifier = Modifier
                .padding(top = 15.sdp),
            onClick = { dispatchEvent(SettingsViewModelEvent.LogOut) }
        ) {
            Text(text = "Log Out")
        }

    }
}

@Preview
@Composable
private fun settingsPrev() {
    SettingsScreen(
        navController = rememberNavController(),
        settingsUiState = SettingsUiState(),
        dispatchEvent = {}
    )
}