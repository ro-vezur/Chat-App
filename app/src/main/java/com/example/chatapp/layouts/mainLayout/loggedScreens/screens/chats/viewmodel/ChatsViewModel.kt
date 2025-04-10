package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chats.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.chatapp.Dtos.chat.ChatUI
import com.example.chatapp.Dtos.chat.LocalChatInfo
import com.example.chatapp.Dtos.chat.chatType.ChatType
import com.example.chatapp.model.db.chatDb.observers.userChatsChanges.ChatChange
import com.example.chatapp.model.db.chatDb.observers.userChatsChanges.ObserveUserChatsChangesUseCase
import com.example.chatapp.model.db.messagesDbUseCases.gets.GetChatMessageUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetCurrentUserIdUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetUserPaginatedChatsUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val getUserPaginatedChatsUseCase: GetUserPaginatedChatsUseCase,
    private val observeUserChatsChangesUseCase: ObserveUserChatsChangesUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getChatMessageUseCase: GetChatMessageUseCase,

    ): ViewModel() {

    private val _navigationEvents = MutableSharedFlow<String>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    private val _chatsUiState: MutableStateFlow<ChatsUiState> = MutableStateFlow(ChatsUiState())
    val chatsUiState: StateFlow<ChatsUiState> = _chatsUiState.asStateFlow()

    private val updatedChats: MutableStateFlow<List<ChatUI>> = MutableStateFlow(listOf())

    private val _paginatedUserChats: MutableStateFlow<PagingData<ChatUI>> = MutableStateFlow(PagingData.empty())
    val paginatedUserChats: StateFlow<PagingData<ChatUI>> = _paginatedUserChats.asStateFlow()

    init {
        Log.d("init chats VM","INIT!")
        observeUserChatChanges()
    }

    fun dispatchEvent(event: ChatsViewModelEvent) = viewModelScope.launch {
        when(event) {
            is ChatsViewModelEvent.FetchUserChats -> fetchChats(event.userId,event.localChats)
            is ChatsViewModelEvent.NavigateTo -> navigateTo(event.route)
            is ChatsViewModelEvent.UpdateSearchField -> updateSearchQuery(event.query)
        }
    }

    private fun navigateTo(route: String) = viewModelScope.launch {
        _navigationEvents.emit(route)
    }

    private fun fetchChats(userId: String,localChats: List<LocalChatInfo> ) = viewModelScope.launch {
        val allChats = getUserPaginatedChatsUseCase(userId,localChats).cachedIn(viewModelScope)

        combine(allChats,updatedChats,_chatsUiState) { all, updated, chatsUiState  ->
            all.filter { it.name?.contains(chatsUiState.searchQuery,true) == true }
                .map { chat ->
                if(chat.id in updated.map { it.id }) {
                    updated.find { it.id == chat.id } ?: ChatUI()
                } else {
                    chat
                }
            }
        }
            .collectLatest { pagingData ->
                _paginatedUserChats.emit(pagingData)
            }

    }

    private fun updateSearchQuery(query: String) = viewModelScope.launch {
        _chatsUiState.update { state ->
            state.copy(searchQuery = query)
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeUserChatChanges() = viewModelScope.launch {
        val userId = getCurrentUserIdUseCase()

        observeUserChatsChangesUseCase(
            userId = userId,
        )
            .collectLatest { chatChange ->
            when(chatChange) {
                is ChatChange.Added -> {

                }
                is ChatChange.Removed -> {

                }
                is ChatChange.Updated -> {
                    val chat = chatChange.chat

                    val userLastReadMessageId = chat.lastReads[userId] ?: ""
                    val lastMessageObject = getChatMessageUseCase(chatChange.chat.id,chatChange.chat.messages.lastOrNull() ?: "")
                    val oppositeUserId = chat.getOppositeUserId(userId) ?: ""
                    val oppositeUser = if(chat.chatType == ChatType.USER) getUserUseCase(oppositeUserId) else null
                    val typingUsersText = when {
                        chat.usersTyping.isEmpty() -> null
                        chat.usersTyping.size == 1 && chat.usersTyping.contains(userId) -> null
                        chat.usersTyping.size == 1 -> "${getUserUseCase(chat.usersTyping.first())?.name} is Typing"
                        chat.usersTyping.size > 1 -> "${chat.usersTyping.filter { it != userId }.size} are Typing"
                        else -> null
                    }

                //    Log.d("userLastReadMessageId",userLastReadMessageId)
                //    Log.d("lastReadMessageObject",lastReadMessageObject.toString())
               //     Log.d("oppositeUserId",oppositeUserId)
                    Log.d("opposite user",oppositeUser.toString())
                 //   Log.d("typingUsersText",typingUsersText.toString())

                    val chatUI = ChatUI(
                        id = chat.id,
                        chatType = chat.chatType,
                        lastMessage = lastMessageObject,
                        isPinned = false,
                        name = oppositeUser?.name ?: chat.name,
                        imageUrl = oppositeUser?.imageUrl ?: chat.imageUrl,
                        userId = oppositeUser?.id,
                        unseenMessagesCount = chat.messages.dropWhile { messageId -> messageId != userLastReadMessageId}.drop(1).size,
                        typingUsersText = typingUsersText,
                        lastUpdateTimestamp = chat.lastUpdateTimestamp
                    )

                    updatedChats.update { state ->
                        val mutableState = state.toMutableList()
                        val stateIds = state.map { it.id }
                        if(stateIds.contains(chatUI.id)) {
                            val chatIndex = stateIds.indexOf(stateIds.find { it == chatUI.id })
                            mutableState[chatIndex] = chatUI
                            Log.d("state",mutableState[chatIndex].typingUsersText.toString())
                            mutableState
                        } else {
                            (state + chatUI).toMutableList()
                        }
                    }
                }
            }
        }
    }
}