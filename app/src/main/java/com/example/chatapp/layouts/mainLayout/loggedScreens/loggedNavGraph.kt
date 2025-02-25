package com.example.chatapp.layouts.mainLayout.loggedScreens

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.chatapp.LocalUser
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.ChatScreen
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.ChatViewModel
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chats.ChatsScreen
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chats.ChatsViewModel
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.FriendsScreen
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.viewmodel.FriendsViewModel
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.viewmodel.FriendsViewModelEvent
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.requestsScreen.FriendsRequestScreen
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.requestsScreen.requestsViewModel.FriendsRequestsViewModel
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.SettingsScreen
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.settingsViewModel.SettingsViewModel
import com.example.chatapp.navigation.ScreenRoutes

fun NavGraphBuilder.loggedNavGraph(navController: NavController) {
    navigation<ScreenRoutes.LoggedScreens>(
        startDestination = ScreenRoutes.LoggedScreens.ChatsRoute
    ) {
        composable<ScreenRoutes.LoggedScreens.ChatsRoute> {

            val chatsViewModel: ChatsViewModel = hiltViewModel()

            val chatsUiState by chatsViewModel.chatsUiState.collectAsStateWithLifecycle()

            ChatsScreen(
                chatsUiState = chatsUiState
            )
        }

        composable<ScreenRoutes.LoggedScreens.FriendRequestsRoute> {

            val friendsRequestsViewModel: FriendsRequestsViewModel = hiltViewModel()
            val friendsRequestsUiState by friendsRequestsViewModel.friendsRequestsUiState.collectAsStateWithLifecycle()

            FriendsRequestScreen(
                friendsRequestsUiState = friendsRequestsUiState,
                dispatchEvent = friendsRequestsViewModel::dispatchEvent
            )
        }

        composable<ScreenRoutes.LoggedScreens.FriendsRoute> {

            val friendsViewModel: FriendsViewModel = hiltViewModel()
            val friendsUiState by friendsViewModel.friendsUiState.collectAsStateWithLifecycle()

            val user = LocalUser.current

            LaunchedEffect(key1 = user.friends) {
                friendsViewModel.dispatchEvent(FriendsViewModelEvent.FetchMyFriends(user.friends))
            }

            FriendsScreen(
                friendsUiState = friendsUiState,
                dispatchEvent = friendsViewModel::dispatchEvent
            )
        }

        composable<ScreenRoutes.LoggedScreens.ChatRoute> {

            val chatViewModel: ChatViewModel = hiltViewModel()

            val chatUiState by chatViewModel.chatUiState.collectAsStateWithLifecycle()

            ChatScreen(
                chatUiState = chatUiState,
            )
        }

        composable<ScreenRoutes.LoggedScreens.SettingsRoute> {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val settingsUiState by settingsViewModel.settingsUiState.collectAsStateWithLifecycle()

            SettingsScreen(
                settingsUiState = settingsUiState,
                dispatchEvent = settingsViewModel::dispatchEvent
            )
        }
    }
}