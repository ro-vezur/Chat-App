package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.settingsScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.LocalUser
import com.example.chatapp.differentScreensSupport.sdp
import com.example.chatapp.helpers.navigation.singleClickNavigate
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.SettingsScreenActionButtons
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.settingsViewModel.SettingsUiState
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.settingsViewModel.SettingsViewModelEvent
import com.example.chatapp.layouts.sharedComponents.images.UserImage
import com.example.chatapp.navigation.ScreenRoutes
import com.example.chatapp.ui.theme.ChatAppTheme

@Composable
fun SettingsMainScreen(
    navController: NavController,
    settingsUiState: SettingsUiState,
    dispatchEvent: (SettingsViewModelEvent) -> Unit,
) {
    val user = LocalUser.current
    val scrollState = rememberScrollState()


    Column(
        modifier = Modifier
            .padding(horizontal = 15.sdp, vertical = 10.sdp)
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.sdp)
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

        SettingsScreenActionButtons.entries.forEach { action ->
            if(action.navigateRoute == null) {
                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = 3.sdp)
                        .clip(CircleShape),
                    color = MaterialTheme.colorScheme.primary,
                    thickness = 2.sdp
                )
            }

            Box(
                modifier = Modifier
            ) {
                ActionCard(
                    action = action,
                    onClick = {
                        if(action.navigateRoute == null) {
                            dispatchEvent(SettingsViewModelEvent.LogOut)
                        } else {
                            navController.navigate(action.navigateRoute)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ActionCard(
    action: SettingsScreenActionButtons,
    onClick: () -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(50.sdp, 90.sdp)
            .shadow(2.sdp, RoundedCornerShape(12.sdp))
            .clip(RoundedCornerShape(12.sdp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onClick() }
            .padding(horizontal = 10.sdp, vertical = 6.sdp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.sdp)
    ) {
        Icon(
            modifier = Modifier
                .size(30.sdp),
            imageVector = action.icon,
            contentDescription = "action icon",
        )

        VerticalDivider(
            modifier = Modifier
                .height(32.sdp)
                .clip(CircleShape),
            thickness = 2.sdp,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            modifier = Modifier
                .weight(1f),
            text = action.text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview
@Composable
private fun settingsPrev() {
    ChatAppTheme {
        Surface {
            SettingsMainScreen(
                navController = rememberNavController(),
                settingsUiState = SettingsUiState(),
                dispatchEvent = {}
            )
        }
    }
}