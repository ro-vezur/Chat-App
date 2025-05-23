package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chats.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.chatapp.Dtos.chat.ChatUI
import com.example.chatapp.Dtos.chat.enums.ChatType
import com.example.chatapp.model.db.chatDb.observers.userChatsChanges.ObserveUserChatsChangesUseCase
import com.example.chatapp.model.db.messagesDbUseCases.gets.GetChatMessageUseCase
import com.example.chatapp.model.db.sealedChanges.ChatChange
import com.example.chatapp.model.db.userDbUsecases.gets.GetCurrentUserIdUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetUserPaginatedChatsUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
        observeUserChatChanges()
        fetchChats()
    }

    fun dispatchEvent(event: ChatsViewModelEvent) = viewModelScope.launch {
        when(event) {
            is ChatsViewModelEvent.NavigateTo -> navigateTo(event.route)
            is ChatsViewModelEvent.UpdateSearchField -> updateSearchQuery(event.query)
        }
    }

    private fun navigateTo(route: String) = viewModelScope.launch {
        _navigationEvents.emit(route)
    }

    private fun fetchChats( ) = viewModelScope.launch {
        val userId = getCurrentUserIdUseCase()
        val user = getUserUseCase(userId)
        val localChats = user?.localChats ?: emptyList()
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

    private fun observeUserChatChanges() = viewModelScope.launch {
        val mainUserId = getCurrentUserIdUseCase()

        observeUserChatsChangesUseCase(
            userId = mainUserId,
        )
            .collectLatest { chatChange ->
            when(chatChange) {
                is ChatChange.Added -> {

                }
                is ChatChange.Removed -> {

                }
                is ChatChange.Updated -> {
                    val chat = chatChange.chat

                    val userLastReadMessageId = chat.lastReads[mainUserId] ?: ""
                    val lastMessageObject = getChatMessageUseCase(chatChange.chat.id,chatChange.chat.messages.lastOrNull() ?: "")
                    val oppositeUserId = chat.getOppositeUserId(mainUserId) ?: ""
                    val oppositeUser = if(chat.chatType == ChatType.USER) getUserUseCase(oppositeUserId) else null
                    val typingUsersText = when {
                        chat.usersTyping.isEmpty() -> null
                        chat.usersTyping.size == 1 && chat.usersTyping.contains(mainUserId) -> null
                        chat.usersTyping.size == 1 -> "${getUserUseCase(chat.usersTyping.first())?.name} is Typing"
                        chat.usersTyping.size > 1 -> "${chat.usersTyping.filter { it != mainUserId }.size} are Typing"
                        else -> null
                    }

                    val chatUI = ChatUI(
                        id = chat.id,
                        chatType = chat.chatType,
                        lastMessage = lastMessageObject,
                        isPinned = false,
                        name = oppositeUser?.getOppositeUserName(mainUserId) ?: chat.name,
                        imageUrl = oppositeUser?.getOppositeUserImage(mainUserId) ?: chat.imageUrl,
                        user = oppositeUser,
                        unseenMessagesCount = chat.messages.dropWhile { messageId -> messageId != userLastReadMessageId}.drop(1).size,
                        typingUsersText = typingUsersText,
                        lastUpdateTimestamp = chat.lastUpdateTimestamp
                    )

                    updatedChats.update { state ->
                        val mutableState = state.toMutableList()
                        val stateIds = state.map { it.id }
                        if(stateIds.contains(chatUI.id)) {
                            val chatIndex = stateIds.indexOf(chatUI.id)
                            mutableState[chatIndex] = chatUI
                            mutableState
                        } else {
                            state + chatUI
                        }
                    }
                }
            }
        }
    }
}