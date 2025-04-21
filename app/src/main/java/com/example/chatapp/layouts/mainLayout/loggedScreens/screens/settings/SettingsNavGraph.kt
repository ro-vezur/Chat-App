package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.chatapp.helpers.navigation.navigateBack
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.editProfileScreen.EditProfileScreen
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.editProfileScreen.EditProfileViewModel
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.settingsScreens.SettingsMainScreen
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.settingsScreens.SettingsPrivacyScreen
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.settingsViewModel.SettingsViewModel
import com.example.chatapp.navigation.ScreenRoutes

fun NavGraphBuilder.settingsNavGraph(
    navController: NavController,
    showBottomBar: (Boolean) -> Unit,
) {
    navigation<ScreenRoutes.LoggedScreens.SettingsGraphRoute>(
        startDestination = ScreenRoutes.LoggedScreens.SettingsGraphRoute.MainSettingsRoute
    ) {
        composable<ScreenRoutes.LoggedScreens.SettingsGraphRoute.MainSettingsRoute> {
            showBottomBar(true)
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val settingsUiState by settingsViewModel.settingsUiState.collectAsStateWithLifecycle()

            SettingsMainScreen(
                navController = navController,
                settingsUiState = settingsUiState,
                dispatchEvent = settingsViewModel::dispatchEvent
            )
        }

        composable<ScreenRoutes.LoggedScreens.SettingsGraphRoute.MainSettingsRoute.PrivacyRoute> {
            val settingsBackStackEntry = navController.getBackStackEntry(ScreenRoutes.LoggedScreens.SettingsGraphRoute)
            val settingsViewModel: SettingsViewModel = hiltViewModel(settingsBackStackEntry)

            SettingsPrivacyScreen(
                turnBack = { navController.navigateBack() },
                dispatchEvent = settingsViewModel::dispatchEvent
            )
        }

        composable<ScreenRoutes.LoggedScreens.SettingsGraphRoute.MainSettingsRoute.NotificationsRoute> {

        }

        composable<ScreenRoutes.LoggedScreens.SettingsGraphRoute.MainSettingsRoute.MessagesRoute> {

        }

        composable<ScreenRoutes.LoggedScreens.SettingsGraphRoute.MainSettingsRoute.MediaRoute> {

        }

        composable<ScreenRoutes.LoggedScreens.SettingsGraphRoute.MainSettingsRoute.AppearanceRoute> {

        }

        composable<ScreenRoutes.LoggedScreens.SettingsGraphRoute.EditProfileRoute>(
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(450),
                    initialOffsetX = { it }
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(550),
                    targetOffsetX = { it }
                )
            }
        ) {
            val editProfileViewModel: EditProfileViewModel = hiltViewModel()
            showBottomBar(false)

            EditProfileScreen(
                navController = navController,
                uploadImage = editProfileViewModel::uploadImageToServer,
                updateUser = { newUser ->
                    editProfileViewModel.updateUser(
                        user = newUser,
                        onSuccess = { navController.navigateBack() }
                    )
                },
                deleteImage = { imageBody ->
                   // navController.navigateBack()
                    editProfileViewModel.deleteImage(imageBody)
                }
            )
        }
    }
}