package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.requestsScreen.requestsViewModel

import com.example.chatapp.Dtos.user.User
import com.example.chatapp.others.ResourceResult

data class FriendsRequestsUiState(
    val requestsResult: ResourceResult<List<User>> = ResourceResult.Loading(),
    val searchQuery: String = ""
)