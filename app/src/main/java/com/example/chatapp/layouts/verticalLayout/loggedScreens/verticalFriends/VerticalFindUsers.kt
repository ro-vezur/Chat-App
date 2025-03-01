package com.example.chatapp.layouts.verticalLayout.loggedScreens.verticalFriends

import android.util.Log
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
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.LocalUser
import com.example.chatapp.R
import com.example.chatapp.differentScreensSupport.sdp
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.viewmodel.FriendsUiState
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.viewmodel.FriendsViewModelEvent
import com.example.chatapp.layouts.sharedComponents.inputFields.CustomSearchBar
import com.example.chatapp.layouts.sharedComponents.resultScreens.LoadingScreen
import com.example.chatapp.layouts.verticalLayout.sharedComponents.VerticalUseCardActionButton
import com.example.chatapp.others.ResourceResult
import com.example.chatapp.ui.theme.FriendColor
import kotlinx.coroutines.delay

@Composable
fun VerticalFindUsers(
    friendsUiState: FriendsUiState,
    dispatchEvent: (FriendsViewModelEvent) -> Unit,
) {

    LaunchedEffect(key1 = friendsUiState.searchQuery) {
        if(friendsUiState.searchQuery.isBlank()) return@LaunchedEffect

        delay(1000)
        dispatchEvent(FriendsViewModelEvent.FindUsers(friendsUiState.searchQuery))
    }
    
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
                Log.d("find users result",friendsUiState.findFriendsResult.message.toString())
                Log.d("find users data",friendsUiState.findFriendsResult.data.toString())
            }
        )

        Box(
            modifier = Modifier
                .padding(top = 15.sdp)
        ) {
            if(friendsUiState.searchQuery.isNotEmpty()) {
                when(friendsUiState.findFriendsResult) {
                    is ResourceResult.Loading -> {
                        LoadingScreen(modifier = Modifier.fillMaxSize())
                    }
                    is ResourceResult.Success -> {
                        friendsUiState.findFriendsResult.data?.let { users ->
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth(0.92f),
                                verticalArrangement = Arrangement.spacedBy(12.sdp)
                            ) {
                                items(users) {user ->
                                    SearchedUserCard(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(70.sdp)
                                            .clip(RoundedCornerShape(25))
                                            .background(MaterialTheme.colorScheme.surface)
                                            .padding(horizontal = 10.sdp),
                                        searchedUser = user,
                                        dispatchEvent = dispatchEvent,
                                    )
                                } 
                            }
                        }
                    }
                    is ResourceResult.Error -> {

                    }
                }
            } else {

            }
        }

    }

}

@Composable
private fun SearchedUserCard(
    modifier: Modifier = Modifier,
    searchedUser: User,
    dispatchEvent: (FriendsViewModelEvent) -> Unit,
) {
    val mainUser = LocalUser.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(searchedUser.imageUrl != null) {
            AsyncImage(
                modifier = Modifier
                    .size(52.sdp)
                    .clip(CircleShape),
                model = searchedUser.imageUrl,
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
            text = searchedUser.name,
            style = typography.labelLarge
        )

        Spacer(modifier = Modifier.weight(1f))

        when {
            mainUser.friends.contains(searchedUser.id) -> {
                VerticalUseCardActionButton(
                    icon = Icons.Filled.EmojiEmotions,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = FriendColor,
                        contentColor = Color.White,
                    ),
                    onClick = {

                    }
                )
            }
            mainUser.sentRequestsToUsers.contains(searchedUser.id) -> {
                VerticalUseCardActionButton(
                    icon = Icons.Filled.Pending,
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary,),
                    onClick = {

                    }
                )
            }
            else -> {
                VerticalUseCardActionButton(
                    icon = Icons.Filled.Send,
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary,),
                    onClick = {
                        dispatchEvent(FriendsViewModelEvent.SendFriendRequest(mainUser,searchedUser))
                    }
                )
            }
        }
    }
}