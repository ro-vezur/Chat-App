package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.requestsScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatapp.differentScreensSupport.checkIsPortrait
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.requestsScreen.requestsViewModel.FriendsRequestViewModelEvent
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.requestsScreen.requestsViewModel.FriendsRequestsUiState
import com.example.chatapp.layouts.verticalLayout.loggedScreens.VerticalFriendsRequestsScreen
import com.example.chatapp.ui.theme.ChatAppTheme

@Composable
fun FriendsRequestScreen(
    friendsRequestsUiState: FriendsRequestsUiState,
    dispatchEvent: (FriendsRequestViewModelEvent) -> Unit,
) {

    if(checkIsPortrait()) {
        VerticalFriendsRequestsScreen(
            friendsRequestsUiState = friendsRequestsUiState,
            dispatchEvent = dispatchEvent,
        )
    } else {

    }
}

@Preview
@Composable
private fun previewFriendsRequestsScreen() {
    
    ChatAppTheme(
        darkTheme = true
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            FriendsRequestScreen(
                friendsRequestsUiState = FriendsRequestsUiState(),
                dispatchEvent = {}
            )
        }
    }
}