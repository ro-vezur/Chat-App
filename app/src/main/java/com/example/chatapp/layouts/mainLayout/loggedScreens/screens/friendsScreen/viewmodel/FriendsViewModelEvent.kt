package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.viewmodel

import com.example.chatapp.Dtos.User

sealed class FriendsViewModelEvent() {
    class SetTabIndex(val index: Int): FriendsViewModelEvent()
    class UpdateSearchField(val query: String): FriendsViewModelEvent()
    class FindUsers(val query: String): FriendsViewModelEvent()
    class FetchMyFriends(val usersList: List<String> ) : FriendsViewModelEvent()
    class SendFriendRequest(val sender: User, val receiver: User): FriendsViewModelEvent()
}