package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.LocalUser
import com.example.chatapp.differentScreensSupport.sdp
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.viewmodel.FriendsUiState
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.viewmodel.FriendsViewModelEvent
import com.example.chatapp.layouts.sharedComponents.images.UserImage
import com.example.chatapp.layouts.sharedComponents.inputFields.CustomSearchBar
import com.example.chatapp.layouts.sharedComponents.resultScreens.LoadingScreen
import com.example.chatapp.layouts.verticalLayout.sharedComponents.UserCardActionButton
import com.example.chatapp.others.Resource

@Composable
fun MyFriendsScreen(
    friendsUiState: FriendsUiState,
    dispatchEvent: (FriendsViewModelEvent) -> Unit,
) {
    val mainUser = LocalUser.current

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        CustomSearchBar(
            query = friendsUiState.searchQuery,
            placeHolder = "Enter User's Name to Find",
            onSearchQueryChange = { query ->
                dispatchEvent(FriendsViewModelEvent.UpdateSearchField(query))
            }
        )

        Box(
            modifier = Modifier
                .padding(top = 15.sdp)
        ) {
            when (friendsUiState.myFriendsResult) {
                is Resource.Loading -> {
                    LoadingScreen(modifier = Modifier.fillMaxSize())
                }

                is Resource.Success -> {
                    friendsUiState.myFriendsResult.data?.let { users ->

                        val rememberUsersList by remember(friendsUiState.searchQuery,users) {
                            mutableStateOf(users.filter { it.name.contains(friendsUiState.searchQuery,true) })
                        }

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth(0.92f),
                            verticalArrangement = Arrangement.spacedBy(12.sdp)
                        ) {
                            items(rememberUsersList) { user ->
                                SearchedUserCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(70.sdp)
                                        .clip(RoundedCornerShape(25))
                                        .background(MaterialTheme.colorScheme.surface)
                                        .padding(horizontal = 10.sdp),
                                    user = user,
                                    dispatchEvent = dispatchEvent,
                                )
                            }
                        }
                    }
                }

                is Resource.Error -> {

                }
            }
        }
    }

    if(friendsUiState.friendToDelete != null) {
        DeleteFriendAlertDialog(
            friendToDelete = friendsUiState.friendToDelete,
            onConfirm = {
                dispatchEvent(
                    FriendsViewModelEvent.DeleteFriend(
                        friendId = friendsUiState.friendToDelete.id,
                        onSuccess = { dispatchEvent(FriendsViewModelEvent.SetFriendIdToDelete(null)) }
                    )
                )
            },
            onDismiss = { dispatchEvent(FriendsViewModelEvent.SetFriendIdToDelete(null)) }
        )
    }
}

@Composable
private fun SearchedUserCard(
    modifier: Modifier = Modifier,
    user: User,
    dispatchEvent: (FriendsViewModelEvent) -> Unit,
) {
    val mainUser = LocalUser.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        UserImage(
            modifier = Modifier
                .size(52.sdp)
                .clip(CircleShape),
            imageUrl = user.imageUrl
        )

        Text(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.sdp),
            text = user.name,
            style = MaterialTheme.typography.labelMedium
        )

        UserCardActionButton(
            icon = Icons.Filled.Delete,
            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.error),
            onClick = {
                dispatchEvent(FriendsViewModelEvent.SetFriendIdToDelete(user))
            }
        )

        UserCardActionButton(
            icon = Icons.Filled.Chat,
            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
            onClick = {
                dispatchEvent(FriendsViewModelEvent.AddChat(listOf(mainUser.id, user.id)))
            }
        )
    }
}