package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.viewmodel

import com.example.chatapp.Dtos.User
import com.example.chatapp.others.ResourceResult

data class FriendsUiState(
    val myFriendsResult: ResourceResult<List<User>> = ResourceResult.Loading(),
    val findFriendsResult: ResourceResult<List<User>> = ResourceResult.Loading(),
    val selectedTabIndex: Int = 0,
    val searchQuery: String = "",
)
