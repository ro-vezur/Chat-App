package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.oneToOneChat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.example.chatapp.Dtos.chat.ChatItem
import com.example.chatapp.Dtos.chat.LocalChatInfo
import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.model.db.chatDb.ChatPagingRepository
import com.example.chatapp.model.db.chatDb.observers.ObserveChatUseCase
import com.example.chatapp.model.db.messagesDbUseCases.posts.AddMessageUseCase
import com.example.chatapp.model.db.userDbUsecases.observers.ObserveUserUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.AddLocalChatInfoUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = OneToOneChatViewModel.Factory::class)
class OneToOneChatViewModel @AssistedInject constructor(
    @Assisted("chatId") private val chatId: String,
    @Assisted("userId") private val userId: String,
    private val observeUserUseCase: ObserveUserUseCase,
    private val observeChatUseCase: ObserveChatUseCase,
    private val addMessageUseCase: AddMessageUseCase,
    private val addLocalChatInfoUseCase: AddLocalChatInfoUseCase,
    private val chatPagingRepository: ChatPagingRepository,
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("chatId") chatId: String,
            @Assisted("userId") userId: String
        ): OneToOneChatViewModel
    }

    private val _chatUiState: MutableStateFlow<OneToOneChatUiState> = MutableStateFlow(
        OneToOneChatUiState()
    )
    val chatUiState: StateFlow<OneToOneChatUiState> = _chatUiState.asStateFlow()

    val paginatedMessages = chatPagingRepository.getPaginatedMessages(chatId)
        .map { pagingData ->
            pagingData
                .map { message -> ChatItem.MessageItem(message) }
                .insertSeparators { before: ChatItem.MessageItem?, after: ChatItem.MessageItem? ->
                    when {
                        before == null -> ChatItem.DateHeader("HEADER")
                        after == null -> before.message.formatDate()
                        before.message.formatDate() != after.message.formatDate() -> ChatItem.DateHeader(after.message.formatDate())
                        else -> null

                    }
                }
        }
        .cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            observeUser()
            observeChat()
            chatPagingRepository.messagesListener(chatId)
        }
    }
    private fun observeUser() = viewModelScope.launch {
        observeUserUseCase(userId).collectLatest { user ->
            _chatUiState.update { state ->
                state.copy(user = user)
            }
        }
    }

    private fun observeChat() = viewModelScope.launch {
        observeChatUseCase(chatId).collectLatest { chat ->
            _chatUiState.update { currentState ->
                currentState.copy(chat = chat)
            }
        }
    }

    fun dispatchEvent(event: OneToOneChatViewModelEvent) = viewModelScope.launch {
        when(event) {
            is OneToOneChatViewModelEvent.SendMessage -> sendMessage(event.message)
            is OneToOneChatViewModelEvent.OnEnterQueryChange -> onEnterQueryChange(event.query)
            is OneToOneChatViewModelEvent.AddLocalChatInfo -> addLocalChatInfo(event.userId,event.localChatInfo)
        }
    }

    private fun addLocalChatInfo(userId: String,localChatInfo: LocalChatInfo) = viewModelScope.launch {
        addLocalChatInfoUseCase(userId, localChatInfo)
    }

    private fun sendMessage(message: Message) = viewModelScope.launch {
        addMessageUseCase(message.copy(chatId = chatId))
    }

    private fun onEnterQueryChange(query: String) = viewModelScope.launch {
        _chatUiState.update { state ->
            state.copy(sendMessageQuery = query)
        }
    }

}