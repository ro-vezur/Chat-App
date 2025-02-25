package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.requestsScreen.requestsViewModel

sealed class FriendsRequestViewModelEvent {
    class AcceptFriendRequest(val acceptorId: String, val newFriendId: String): FriendsRequestViewModelEvent()
    class DeclineFriendRequest(val requestUserId: String): FriendsRequestViewModelEvent()
    class FetchFriendRequests(val ids: List<String> ): FriendsRequestViewModelEvent()
    class OnSearchQuery(val query: String): FriendsRequestViewModelEvent()
}