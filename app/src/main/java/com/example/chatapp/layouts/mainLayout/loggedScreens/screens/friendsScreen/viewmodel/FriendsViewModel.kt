package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.Dtos.User
import com.example.chatapp.Dtos.notification.NotificationBody
import com.example.chatapp.Dtos.notification.NotificationData
import com.example.chatapp.Dtos.notification.SendNotificationDto
import com.example.chatapp.model.db.userDbUsecases.gets.FindUsersByNameUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetUsersListWithIdsUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.SendFriendRequestUseCase
import com.example.chatapp.model.services.messanging.SendRemoteNotificationUseCase
import com.example.chatapp.others.ResourceResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val findUsersByNameUseCase: FindUsersByNameUseCase,
    private val getUsersListWithIdsUseCase: GetUsersListWithIdsUseCase,
    private val sendFriendRequestUseCase: SendFriendRequestUseCase,
    private val sendRemoteNotificationUseCase: SendRemoteNotificationUseCase
): ViewModel() {

    private val _friendsUiState: MutableStateFlow<FriendsUiState> = MutableStateFlow(FriendsUiState())
    val friendsUiState: StateFlow<FriendsUiState> = _friendsUiState.asStateFlow()

    fun dispatchEvent(event: FriendsViewModelEvent) {
        when(event) {
            is FriendsViewModelEvent.SetTabIndex -> { setTabIndex(event.index) }
            is FriendsViewModelEvent.UpdateSearchField -> { updateSearchField(event.query) }
            is FriendsViewModelEvent.FindUsers -> { findUsers(event.query) }
            is FriendsViewModelEvent.FetchMyFriends -> { fetchMyFriends(event.usersList) }
            is FriendsViewModelEvent.SendFriendRequest -> { sendFriendRequest(event.sender,event.receiver) }
        }
    }

    private fun setTabIndex(index: Int) = viewModelScope.launch {
        _friendsUiState.emit(
            _friendsUiState.value.copy(
                selectedTabIndex = index,
                findFriendsResult = ResourceResult.Loading(),
                searchQuery = ""
            )
        )
    }

    private fun updateSearchField(query: String) = viewModelScope.launch {
        _friendsUiState.emit(
            _friendsUiState.value.copy(
                findFriendsResult = ResourceResult.Loading(),
                searchQuery = query
            )
        )
    }

     private fun fetchMyFriends(ids: List<String>) = viewModelScope.launch {
         getUsersListWithIdsUseCase(ids).collectLatest { result ->
             _friendsUiState.emit(
                 _friendsUiState.value.copy(
                     myFriendsResult = result
                 )
             )
         }
    }

    private fun findUsers(query: String) = viewModelScope.launch {
        findUsersByNameUseCase(query).collectLatest { result ->
            _friendsUiState.emit(
                _friendsUiState.value.copy(
                    findFriendsResult = result
                )
            )
        }
    }

    private fun sendFriendRequest(sender: User, receiver: User) = viewModelScope.launch {
        val sendFriendRequestTask = sendFriendRequestUseCase(sender,receiver)

        Log.d("is send friend request successful?",sendFriendRequestTask.isSuccessful.toString())

        if(sendFriendRequestTask.isSuccessful) {
            receiver.fcmTokens.forEach { token ->
                sendRemoteNotificationUseCase(
                    sendNotificationDto = SendNotificationDto(
                        token = token,
                        topic = null,
                        notificationBody = NotificationBody(
                            title = "Friend Request",
                            body = "${sender.name} Sent You Friend Request!"
                        ),
                        data = NotificationData(
                            senderId = sender.id,
                            type = "friend_request"
                        )
                    )
                )
            }
        }
     }
}