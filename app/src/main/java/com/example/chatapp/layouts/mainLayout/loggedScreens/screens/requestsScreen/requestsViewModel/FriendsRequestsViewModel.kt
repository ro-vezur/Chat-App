package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.requestsScreen.requestsViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.db.userDbUsecases.gets.GetUsersListWithIdsUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.friendRequest.AcceptFriendRequestUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.friendRequest.DeclineFriendRequestUseCase
import com.example.chatapp.others.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsRequestsViewModel @Inject constructor(
    private val getUsersListWithIdsUseCase: GetUsersListWithIdsUseCase,
    private val declineFriendRequestUseCase: DeclineFriendRequestUseCase,
    private val acceptFriendRequestUseCase: AcceptFriendRequestUseCase,
): ViewModel() {

    private val _friendsRequestsUiState: MutableStateFlow<FriendsRequestsUiState> = MutableStateFlow(
        FriendsRequestsUiState()
    )
    val friendsRequestsUiState: StateFlow<FriendsRequestsUiState> = _friendsRequestsUiState.asStateFlow()

    fun dispatchEvent(event: FriendsRequestViewModelEvent) = viewModelScope.launch {
        when(event) {
            is FriendsRequestViewModelEvent.AcceptFriendRequest -> acceptFriendRequest(event.acceptorId,event.newFriendId)
            is FriendsRequestViewModelEvent.DeclineFriendRequest -> declineFriendRequest(event.requestUserId)
            is FriendsRequestViewModelEvent.FetchFriendRequests -> fetchFriendRequests(event.ids)
            is FriendsRequestViewModelEvent.OnSearchQuery -> onSearchQueryChange(event.query)
        }
    }

    private fun acceptFriendRequest(acceptorId: String, newFriendId: String) = viewModelScope.launch {
        acceptFriendRequestUseCase(acceptorId,newFriendId)
    }

    private fun declineFriendRequest(requestUserId: String) = viewModelScope.launch {
        Log.d("request user id",requestUserId)
        declineFriendRequestUseCase(requestUserId)
    }

    private fun fetchFriendRequests(ids: List<String>) = viewModelScope.launch {
        getUsersListWithIdsUseCase(ids,_friendsRequestsUiState.value.searchQuery).collectLatest { result ->
            _friendsRequestsUiState.emit(
                _friendsRequestsUiState.value.copy(
                    requestsResult = result
                )
            )
        }
    }

    private fun onSearchQueryChange(query: String) = viewModelScope.launch {
        _friendsRequestsUiState.emit(
            _friendsRequestsUiState.value.copy(
                searchQuery = query,
                requestsResult = Resource.Loading()
            )
        )
    }

}