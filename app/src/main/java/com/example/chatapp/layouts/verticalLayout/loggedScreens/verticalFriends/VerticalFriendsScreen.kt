package com.example.chatapp.layouts.verticalLayout.loggedScreens.verticalFriends

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chatapp.differentScreensSupport.sdp
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.FriendScreenTabs
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.viewmodel.FriendsUiState
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.viewmodel.FriendsViewModelEvent

@Composable
fun VerticalFriendsScreen(
    friendsUiState: FriendsUiState,
    dispatchEvent: (FriendsViewModelEvent) -> Unit,
) {

    Scaffold(
        topBar = {
            TabRow(
                selectedTabIndex = friendsUiState.selectedTabIndex
            ) {
                FriendScreenTabs.entries.forEachIndexed { index, tab ->
                    val isSelected = friendsUiState.selectedTabIndex == index

                    Tab(
                        selected = isSelected,
                        onClick = { dispatchEvent(FriendsViewModelEvent.SetTabIndex(index)) }
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(vertical = 20.sdp),
                            text = tab.title,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 8.dp)
                .fillMaxSize()
        ) {
            when(friendsUiState.selectedTabIndex) {
                0 -> {
                    VerticalMyFriendsScreen(
                        friendsUiState = friendsUiState,
                        dispatchEvent = dispatchEvent,
                    )
                }
                1 -> {
                    VerticalFindUsers(
                        friendsUiState = friendsUiState,
                        dispatchEvent = dispatchEvent,
                    )
                }
            }
        }
    }

}