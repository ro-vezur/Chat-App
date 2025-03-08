package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.requestsScreen.requestsViewModel

import com.example.chatapp.Dtos.user.User
import com.example.chatapp.others.Resource

data class FriendsRequestsUiState(
    val requestsResult: Resource<List<User>> = Resource.Loading(),
    val searchQuery: String = ""
)