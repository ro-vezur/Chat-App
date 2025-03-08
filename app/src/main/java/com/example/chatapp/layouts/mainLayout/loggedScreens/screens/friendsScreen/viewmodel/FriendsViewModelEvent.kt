package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.viewmodel

import com.example.chatapp.Dtos.user.User

sealed class FriendsViewModelEvent() {
    class OnNavigate(val route: String): FriendsViewModelEvent()
    class SetTabIndex(val index: Int): FriendsViewModelEvent()
    class UpdateSearchField(val query: String): FriendsViewModelEvent()
    class FindUsers(val query: String): FriendsViewModelEvent()
    class FetchMyFriends(val usersList: List<String> ) : FriendsViewModelEvent()
    class SendFriendRequest(val sender: User, val receiver: User): FriendsViewModelEvent()
    class DeleteFriend(val friendId: String, val onSuccess: () -> Unit): FriendsViewModelEvent()
    class SetFriendIdToDelete(val friend: User?): FriendsViewModelEvent()
    class AddChat(val userIds: List<String>): FriendsViewModelEvent()
}