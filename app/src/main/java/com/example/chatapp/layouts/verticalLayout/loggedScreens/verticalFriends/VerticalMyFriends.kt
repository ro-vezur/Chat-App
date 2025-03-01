package com.example.chatapp.layouts.verticalLayout.loggedScreens.verticalFriends

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.R
import com.example.chatapp.differentScreensSupport.sdp
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.DeleteFriendAlertDialog
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.viewmodel.FriendsUiState
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.viewmodel.FriendsViewModelEvent
import com.example.chatapp.layouts.sharedComponents.inputFields.CustomSearchBar
import com.example.chatapp.layouts.sharedComponents.resultScreens.LoadingScreen
import com.example.chatapp.layouts.verticalLayout.sharedComponents.VerticalUseCardActionButton
import com.example.chatapp.others.ResourceResult

@Composable
fun VerticalMyFriendsScreen(
    friendsUiState: FriendsUiState,
    dispatchEvent: (FriendsViewModelEvent) -> Unit,
) {

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
                is ResourceResult.Loading -> {
                    LoadingScreen(modifier = Modifier.fillMaxSize())
                }

                is ResourceResult.Success -> {
                    friendsUiState.myFriendsResult.data?.let { users ->

                        val rememberUsersList by remember(friendsUiState.searchQuery) {
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

                is ResourceResult.Error -> {

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

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(user.imageUrl != null) {
            AsyncImage(
                modifier = Modifier
                    .size(52.sdp)
                    .clip(CircleShape),
                model = user.imageUrl,
                contentDescription = "user image",
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                modifier = Modifier
                    .size(52.sdp)
                    .clip(CircleShape),
                painter = painterResource(id = R.drawable.empty_profile),
                contentDescription = "empty user image",
                contentScale = ContentScale.Crop
            )
        }


        Text(
            modifier = Modifier
                .padding(horizontal = 15.sdp),
            text = user.name,
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(modifier = Modifier.weight(1f))

        VerticalUseCardActionButton(
            icon = Icons.Filled.Delete,
            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.error),
            onClick = {
                dispatchEvent(FriendsViewModelEvent.SetFriendIdToDelete(user))
            }
        )

        VerticalUseCardActionButton(
            icon = Icons.Filled.Chat,
            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
            onClick = {

            }
        )
    }
}