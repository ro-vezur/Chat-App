package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.requestsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatapp.Dtos.requests.FriendRequest
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.LocalUser
import com.example.chatapp.differentScreensSupport.sdp
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.requestsScreen.requestsViewModel.FriendsRequestViewModelEvent
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.requestsScreen.requestsViewModel.FriendsRequestsUiState
import com.example.chatapp.layouts.sharedComponents.images.UserImage
import com.example.chatapp.layouts.sharedComponents.inputFields.CustomSearchBar
import com.example.chatapp.layouts.sharedComponents.resultScreens.LoadingScreen
import com.example.chatapp.layouts.verticalLayout.sharedComponents.VerticalUseCardActionButton
import com.example.chatapp.others.Resource
import com.example.chatapp.ui.theme.ChatAppTheme
import com.example.chatapp.ui.theme.FriendColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FriendsRequestScreen(
    friendsRequestsUiState: FriendsRequestsUiState,
    dispatchEvent: (FriendsRequestViewModelEvent) -> Unit,
) {
    val mainUser = LocalUser.current

    LaunchedEffect(key1 = mainUser.requests,key2 = friendsRequestsUiState.searchQuery) {
        delay(650)
        val requestUserIds = mainUser.requests.map { it.userId }
        dispatchEvent(FriendsRequestViewModelEvent.FetchFriendRequests(requestUserIds))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        CustomSearchBar(
            query = friendsRequestsUiState.searchQuery,
            placeHolder = "Enter User's Names",
            onSearchQueryChange = { query -> dispatchEvent(FriendsRequestViewModelEvent.OnSearchQuery(query)) }
        )

        Box(
            modifier = Modifier
                .padding(top = 15.sdp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when(friendsRequestsUiState.requestsResult) {
                is Resource.Loading -> {
                    LoadingScreen(modifier = Modifier.fillMaxSize())
                }
                is Resource.Success -> {
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
                is Resource.Error -> {

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

        UserImage(
            modifier = Modifier
                .size(50.sdp)
                .clip(CircleShape),
            imageUrl = user.imageUrl
        )

        Text(
            modifier = Modifier
                .padding(horizontal = 10.sdp),
            text = user.name,
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(modifier = Modifier.weight(1f))

        VerticalUseCardActionButton(
            icon = Icons.Filled.Clear,
            colors = IconButtonDefaults.iconButtonColors(containerColor =  MaterialTheme.colorScheme.error),
            onClick = {
                scope.launch {
                    dispatchEvent(FriendsRequestViewModelEvent.DeclineFriendRequest(request.userId))
                }
            }
        )

        VerticalUseCardActionButton(
            icon = Icons.Filled.Check,
            colors = IconButtonDefaults.iconButtonColors(containerColor = FriendColor),
            onClick = {
                scope.launch {
                    dispatchEvent(FriendsRequestViewModelEvent.AcceptFriendRequest(mainUser.id,user.id))
                }
            }
        )
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