package com.example.chatapp.layouts.mainLayout.loggedScreens

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.chatapp.LocalUser
import com.example.chatapp.helpers.navigation.singleClickNavigate
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.oneToOneChat.OneToOneChatScreen
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.oneToOneChat.viewmodel.OneToOneChatViewModel
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chats.ChatsScreen
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chats.viewmodel.ChatsViewModel
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.FriendsScreen
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.viewmodel.FriendsViewModel
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.viewmodel.FriendsViewModelEvent
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.requestsScreen.FriendsRequestScreen
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.requestsScreen.requestsViewModel.FriendsRequestsViewModel
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.settingsNavGraph
import com.example.chatapp.navigation.ScreenRoutes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.loggedNavGraph(
    navController: NavController,
    updateBottomBarState: (Boolean) -> Unit,
) {
    navigation<ScreenRoutes.LoggedScreens>(
        startDestination = ScreenRoutes.LoggedScreens.ChatsRoute
    ) {
        composable<ScreenRoutes.LoggedScreens.ChatsRoute> {
            updateBottomBarState(true)

            val mainUser = LocalUser.current
            val chatsViewModel: ChatsViewModel = hiltViewModel()
            val chatsUiState by chatsViewModel.chatsUiState.collectAsStateWithLifecycle()
            val paginatedUserChats = chatsViewModel.paginatedUserChats.collectAsLazyPagingItems()

            LaunchedEffect(Unit) {
                chatsViewModel.navigationEvents.collectLatest { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            }

            ChatsScreen(
                chatsUiState = chatsUiState,
                paginatedUserChats = paginatedUserChats,
                dispatchEvent = chatsViewModel::dispatchEvent,
            )
        }

        composable<ScreenRoutes.LoggedScreens.FriendRequestsRoute> {
            updateBottomBarState(true)

            val friendsRequestsViewModel: FriendsRequestsViewModel = hiltViewModel()
            val friendsRequestsUiState by friendsRequestsViewModel.friendsRequestsUiState.collectAsStateWithLifecycle()

            FriendsRequestScreen(
                friendsRequestsUiState = friendsRequestsUiState,
                dispatchEvent = friendsRequestsViewModel::dispatchEvent
            )
        }

        composable<ScreenRoutes.LoggedScreens.FriendsRoute> {
            updateBottomBarState(true)

            val friendsViewModel: FriendsViewModel = hiltViewModel()
            val friendsUiState by friendsViewModel.friendsUiState.collectAsStateWithLifecycle()

            val user = LocalUser.current

            LaunchedEffect(Unit) {
                friendsViewModel.navigationEvents.collectLatest { route ->
                    navController.singleClickNavigate(route)
                }
            }

            LaunchedEffect(key1 = user.friends) {
                delay(350)
                friendsViewModel.dispatchEvent(FriendsViewModelEvent.FetchMyFriends(user.friends))
            }

            FriendsScreen(
                friendsUiState = friendsUiState,
                dispatchEvent = friendsViewModel::dispatchEvent
            )
        }

        composable(
            route = ScreenRoutes.LoggedScreens.OneToOneChatRoute.ROUTE,
            arguments = listOf(
                navArgument("chatId") { type = NavType.StringType },
                navArgument("userId") { type = NavType.StringType },
            )
        ) { backstackEntry ->

            updateBottomBarState(false)

            val chatId = backstackEntry.arguments?.getString("chatId")
            val userId = backstackEntry.arguments?.getString("userId")

            if(chatId != null && userId != null) {
                val chatViewModel = hiltViewModel<OneToOneChatViewModel,OneToOneChatViewModel.Factory> { factory ->
                    factory.create(chatId,userId)
                }
                val chatUiState by chatViewModel.chatUiState.collectAsStateWithLifecycle()
                val sendMessageText by chatViewModel.sendMessageText.collectAsStateWithLifecycle()
                val paginatedMessages = chatViewModel.paginatedMessages.collectAsLazyPagingItems()

                OneToOneChatScreen(
                    chatUiState = chatUiState,
                    sendMessageText = sendMessageText,
                    paginatedMessages = paginatedMessages,
                    navController = navController,
                    dispatchEvent = chatViewModel::dispatchEvent
                )
            }
        }

        settingsNavGraph(
            navController = navController,
            showBottomBar = updateBottomBarState
        )
    }
}