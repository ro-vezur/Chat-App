package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.viewmodel

import com.example.chatapp.Dtos.user.User
import com.example.chatapp.others.Resource

data class FriendsUiState(
    val myFriendsResult: Resource<List<User>> = Resource.Loading(),
    val findFriendsResult: Resource<List<User>> = Resource.Loading(),
    val friendToDelete: User? = null,
    val selectedTabIndex: Int = 0,
    val searchQuery: String = "",
)
