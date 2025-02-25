package com.example.chatapp.layouts.verticalLayout.loggedScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.chatapp.Dtos.User
import com.example.chatapp.Dtos.requests.FriendRequest
import com.example.chatapp.LocalUser
import com.example.chatapp.R
import com.example.chatapp.differentScreensSupport.sdp
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.requestsScreen.requestsViewModel.FriendsRequestViewModelEvent
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.requestsScreen.requestsViewModel.FriendsRequestsUiState
import com.example.chatapp.layouts.sharedComponents.inputFields.CustomSearchBar
import com.example.chatapp.layouts.sharedComponents.resultScreens.LoadingScreen
import com.example.chatapp.others.ResourceResult
import com.example.chatapp.ui.theme.FriendColor
import kotlinx.coroutines.launch

@Composable
fun VerticalFriendsRequestsScreen(
    friendsRequestsUiState: FriendsRequestsUiState,
    dispatchEvent: (FriendsRequestViewModelEvent) -> Unit,
) {
    val mainUser = LocalUser.current
    
    LaunchedEffect(key1 = mainUser.requests) {
        val requestUserIds = mainUser.requests.map { it.userId }
        dispatchEvent(FriendsRequestViewModelEvent.FetchFriendRequests(requestUserIds))
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CustomSearchBar(
                    query = friendsRequestsUiState.searchQuery,
                    placeHolder = "Enter User's Names",
                    onSearchQueryChange = { query -> dispatchEvent(FriendsRequestViewModelEvent.OnSearchQuery(query)) }
                )
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 15.sdp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when(friendsRequestsUiState.requestsResult) {
                is ResourceResult.Loading -> {
                    LoadingScreen(modifier = Modifier.fillMaxSize())
                }
                is ResourceResult.Success -> {
                    friendsRequestsUiState.requestsResult.data?.let { requestsUsersList ->
                        if(requestsUsersList.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth(0.92f)
                                    .fillMaxHeight(),
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.spacedBy(12.sdp)
                            ) {
                                items(requestsUsersList) { user ->
                                    mainUser.requests.find { it.userId == user.id }?.let { request ->
                                        UserRequestCard(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(70.sdp)
                                                .clip(RoundedCornerShape(25))
                                                .background(MaterialTheme.colorScheme.surface)
                                                .padding(horizontal = 10.sdp),
                                            user = user,
                                            request = request,
                                            dispatchEvent = dispatchEvent
                                        )
                                    }
                                }
                            }
                        } else {

                        }
                    }
                }
                is ResourceResult.Error -> {

                }
            }
        }
    }
}

@Composable
private fun UserRequestCard(
    modifier: Modifier = Modifier,
    user: User,
    request: FriendRequest,
    dispatchEvent: (FriendsRequestViewModelEvent) -> Unit,
) {
    val mainUser = LocalUser.current
    val scope = rememberCoroutineScope()

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

        IconButton(
            modifier = Modifier
                .padding(horizontal = 10.sdp)
                .size(45.sdp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = Color.White
            ),
            onClick = {
                scope.launch {
                    dispatchEvent(FriendsRequestViewModelEvent.DeclineFriendRequest(request.userId))
                }
            }
        ) {
            Icon(
                modifier = Modifier
                    .size(28.sdp),
                imageVector = Icons.Filled.Clear,
                contentDescription = "Decline reuqest",
            )
        }

        IconButton(
            modifier = Modifier
                .padding(horizontal = 20.sdp)
                .size(45.sdp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = FriendColor,
                contentColor = Color.White
            ),
            onClick = {
                scope.launch {
                    dispatchEvent(FriendsRequestViewModelEvent.AcceptFriendRequest(mainUser.id,user.id))
                    dispatchEvent(FriendsRequestViewModelEvent.AcceptFriendRequest(user.id,mainUser.id))
                }
            }
        ) {
            Icon(
                modifier = Modifier
                    .size(26.sdp),
                imageVector = Icons.Filled.Check,
                contentDescription = "accept requesta",
            )
        }

    }
}