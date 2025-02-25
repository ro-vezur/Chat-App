package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatapp.differentScreensSupport.checkIsPortrait
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.viewmodel.FriendsUiState
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.viewmodel.FriendsViewModelEvent
import com.example.chatapp.layouts.verticalLayout.loggedScreens.verticalFriends.VerticalFriendsScreen
import com.example.chatapp.ui.theme.ChatAppTheme

@Composable
fun FriendsScreen(
    friendsUiState: FriendsUiState,
    dispatchEvent: (FriendsViewModelEvent) -> Unit,
) {
    if (checkIsPortrait()) {
        VerticalFriendsScreen(
            friendsUiState = friendsUiState,
            dispatchEvent = dispatchEvent,
        )
    } else {

    }
}

@Preview
@Composable
private fun previewFriendsScreen() {
    ChatAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            FriendsScreen(
                friendsUiState = FriendsUiState(),
                dispatchEvent = {}
            )
        }
    }
}