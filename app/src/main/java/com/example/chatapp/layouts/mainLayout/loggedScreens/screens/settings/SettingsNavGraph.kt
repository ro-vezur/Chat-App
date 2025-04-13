package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.editProfileScreen.EditProfileScreen
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.editProfileScreen.EditProfileViewModel
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.settingsViewModel.SettingsViewModel
import com.example.chatapp.navigation.ScreenRoutes

fun NavGraphBuilder.settingsNavGraph(
    navController: NavController,
) {
    navigation<ScreenRoutes.LoggedScreens.SettingsGraphRoute>(
        startDestination = ScreenRoutes.LoggedScreens.SettingsGraphRoute.MainSettingsRoute
    ) {
        composable<ScreenRoutes.LoggedScreens.SettingsGraphRoute.MainSettingsRoute> {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val settingsUiState by settingsViewModel.settingsUiState.collectAsStateWithLifecycle()

            SettingsScreen(
                navController = navController,
                settingsUiState = settingsUiState,
                dispatchEvent = settingsViewModel::dispatchEvent
            )
        }

        composable<ScreenRoutes.LoggedScreens.SettingsGraphRoute.EditProfileRoute> {
            val editProfileViewModel: EditProfileViewModel = hiltViewModel()

            EditProfileScreen(
                uploadImage = editProfileViewModel::uploadImageToServer,
                updateUser = editProfileViewModel::updateUser
            )
        }
    }
}