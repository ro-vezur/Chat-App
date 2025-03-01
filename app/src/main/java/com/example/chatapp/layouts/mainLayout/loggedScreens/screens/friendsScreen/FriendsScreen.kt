package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.differentScreensSupport.checkIsPortrait
import com.example.chatapp.differentScreensSupport.sdp
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

@Composable
fun DeleteFriendAlertDialog(
    friendToDelete: User,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        properties = DialogProperties(

        ),
        onDismissRequest = onDismiss,
        confirmButton = {
            Row(
                modifier = Modifier
                    .height(50.sdp)
                    .fillMaxWidth(),
            ) {
                TextButton(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    onClick = onDismiss
                ) {
                    Text(
                        text = "No",
                        style = MaterialTheme.typography.displaySmall
                    )
                }

                TextButton(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    onClick = onConfirm
                ) {
                    Text(
                        text = "Yes",
                        style = MaterialTheme.typography.displaySmall,
                    )
                }
            }
        },
        text = {
            Text(
                text = "Are You Sure Want to Delete ${friendToDelete.name} From Your Friends List?",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displaySmall
            )
        },
    )
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