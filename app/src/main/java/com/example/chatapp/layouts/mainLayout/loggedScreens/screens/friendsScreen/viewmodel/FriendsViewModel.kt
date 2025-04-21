package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.friendsScreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.Dtos.chat.Chat
import com.example.chatapp.Dtos.chat.chatType.ChatType
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.helpers.time.getCurrentTimeInMillis
import com.example.chatapp.model.db.chatDb.usecases.gets.GetOneToOneChatUseCase
import com.example.chatapp.model.db.chatDb.usecases.posts.AddChatUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.FindUsersByNameUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetUsersListWithIdsUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.DeleteFriendUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.friendRequest.SendFriendRequestUseCase
import com.example.chatapp.navigation.ScreenRoutes
import com.example.chatapp.others.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val findUsersByNameUseCase: FindUsersByNameUseCase,
    private val getUsersListWithIdsUseCase: GetUsersListWithIdsUseCase,
    private val sendFriendRequestUseCase: SendFriendRequestUseCase,
    private val deleteFriendUseCase: DeleteFriendUseCase,
    private val addChatUseCase: AddChatUseCase,
    private val getOneToOneChatUseCase: GetOneToOneChatUseCase
): ViewModel() {

    private val _navigationEvents = MutableSharedFlow<String>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    private val _friendsUiState: MutableStateFlow<FriendsUiState> = MutableStateFlow(FriendsUiState())
    val friendsUiState: StateFlow<FriendsUiState> = _friendsUiState.asStateFlow()

    fun dispatchEvent(event: FriendsViewModelEvent) {
        when(event) {
            is FriendsViewModelEvent.SetTabIndex -> setTabIndex(event.index)
            is FriendsViewModelEvent.UpdateSearchField -> updateSearchField(event.query)
            is FriendsViewModelEvent.FindUsers -> findUsers(event.query)
            is FriendsViewModelEvent.FetchMyFriends -> fetchMyFriends(event.usersList)
            is FriendsViewModelEvent.SendFriendRequest -> sendFriendRequest(event.sender,event.receiver)
            is FriendsViewModelEvent.DeleteFriend -> deleteFriend(event.friendId,event.onSuccess)
            is FriendsViewModelEvent.SetFriendIdToDelete -> setFriendIdToDelete(event.friend)
            is FriendsViewModelEvent.OnNavigate -> onNavigate(event.route)
            is FriendsViewModelEvent.AddChat -> addChat(event.userIds)
        }
    }

    private fun setTabIndex(index: Int) = viewModelScope.launch {
        _friendsUiState.emit(
            _friendsUiState.value.copy(
                selectedTabIndex = index,
                findFriendsResult = Resource.Loading(),
                searchQuery = ""
            )
        )
    }

    private fun updateSearchField(query: String) = viewModelScope.launch {
        _friendsUiState.emit(
            _friendsUiState.value.copy(
                findFriendsResult = Resource.Loading(),
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
        sendFriendRequestUseCase(sender,receiver)
     }

    private fun deleteFriend(friendId: String, onSuccess: () -> Unit) = viewModelScope.launch {
        deleteFriendUseCase(friendId, onSuccess = onSuccess)
    }

    private fun setFriendIdToDelete(friend: User?) = viewModelScope.launch {
        _friendsUiState.emit(
            _friendsUiState.value.copy(friendToDelete = friend)
        )
    }

    private fun onNavigate(route: String)  = viewModelScope.launch {
        _navigationEvents.emit(route)
    }

    private fun addChat(usersIds: List<String>) = viewModelScope.launch {

        val chat = getOneToOneChatUseCase(usersIds = usersIds)

        if(chat == null) {
            val newChatId = UUID.randomUUID()
            val newChat = Chat(
                id = newChatId.toString(),
                createdTimeStamp = getCurrentTimeInMillis(),
                chatType = ChatType.USER,
                users = usersIds.toMutableList()
            )

            addChatUseCase(
                chat = newChat,
                onSuccess = {
                    onNavigate("${ScreenRoutes.LoggedScreens.OneToOneChatRoute.MAIN_ROUTE_PART}/$newChatId/${usersIds[1]}")
                }
            )
        } else {
            onNavigate("${ScreenRoutes.LoggedScreens.OneToOneChatRoute.MAIN_ROUTE_PART}/${chat.id}/${usersIds[1]}")
        }

    }
}