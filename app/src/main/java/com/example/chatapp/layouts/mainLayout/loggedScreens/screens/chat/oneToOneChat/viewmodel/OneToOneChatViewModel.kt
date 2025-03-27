package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.oneToOneChat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.example.chatapp.Dtos.chat.ChatItem
import com.example.chatapp.Dtos.chat.LocalChatInfo
import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.model.db.chatDb.ChatPagingRepository
import com.example.chatapp.model.db.chatDb.observers.ObserveChatUseCase
import com.example.chatapp.model.db.messagesDbUseCases.posts.AddMessageUseCase
import com.example.chatapp.model.db.messagesDbUseCases.posts.SetMessagesReadStatusUseCase
import com.example.chatapp.model.db.messagesDbUseCases.posts.UpdateUserLastSeenMessageIdUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetCurrentUserIdUseCase
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
    private val setMessagesReadStatusUseCase: SetMessagesReadStatusUseCase,
    private val updateUserLastSeenMessageIdUseCase: UpdateUserLastSeenMessageIdUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("chatId") chatId: String,
            @Assisted("userId") userId: String
        ): OneToOneChatViewModel
    }

    private val messagesReadList: MutableList<Message> = mutableListOf()

    private val _chatUiState: MutableStateFlow<OneToOneChatUiState> = MutableStateFlow(
        OneToOneChatUiState()
    )
    val chatUiState: StateFlow<OneToOneChatUiState> = _chatUiState.asStateFlow()

    private val _paginatedMessages = MutableStateFlow<PagingData<ChatItem>>(PagingData.empty())
    val paginatedMessages: StateFlow<PagingData<ChatItem>> = _paginatedMessages.asStateFlow()

    init {
        viewModelScope.launch {
            setPagingData()
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

    private fun setPagingData() = viewModelScope.launch {
        chatPagingRepository.getPaginatedMessages(chatId)
            .map { pagingData ->
                pagingData
                    .map { message -> ChatItem.MessageItem(message) }
                    .insertSeparators { before: ChatItem.MessageItem?, after: ChatItem.MessageItem? ->
                        when {
                            before == null -> null
                            after == null -> ChatItem.DateHeader(before.message.formatDate())
                            before.message.formatDate() != after.message.formatDate() -> ChatItem.DateHeader(before.message.formatDate())
                            after.message.seenBy.contains(getCurrentUserIdUseCase())
                                    && !before.message.seenBy.contains(getCurrentUserIdUseCase())
                                    && before.message.userId != getCurrentUserIdUseCase() -> ChatItem.NewMessagesSeparator()
                            !before.message.seenBy.contains(getCurrentUserIdUseCase())
                                    && before.message.userId != getCurrentUserIdUseCase()
                                    && after.message.userId == getCurrentUserIdUseCase() -> ChatItem.NewMessagesSeparator()
                            else -> null

                        }
                    }
            }
            .cachedIn(viewModelScope)
            .collect {
                _paginatedMessages.emit(it)
            }
    }

    fun dispatchEvent(event: OneToOneChatViewModelEvent) = viewModelScope.launch {
        when(event) {
            is OneToOneChatViewModelEvent.SendMessage -> sendMessage(event.message)
            is OneToOneChatViewModelEvent.OnEnterQueryChange -> onEnterQueryChange(event.query)
            is OneToOneChatViewModelEvent.AddLocalChatInfo -> addLocalChatInfo(event.userId,event.localChatInfo)
            is OneToOneChatViewModelEvent.AddMessageToReadList -> addMessageReadList(event.message,event.userId)
            OneToOneChatViewModelEvent.ClearMessagesReadList -> clearMessagesReadList()
            is OneToOneChatViewModelEvent.SetMessagesReadStatus -> setMessagesReadStatus(event.userId)
            is OneToOneChatViewModelEvent.UpdateUserLastSeenMessage -> updateUserLastSeenMessage(event.userId,event.messageId)
        }
    }

    private fun updateUserLastSeenMessage(userId: String,messageId: String) = viewModelScope.launch {
        updateUserLastSeenMessageIdUseCase(userId,chatId,messageId)
    }

    private fun setMessagesReadStatus(userId: String) = viewModelScope.launch {
        if(messagesReadList.isNotEmpty()) {
            updateUserLastSeenMessageIdUseCase(userId,chatId,messagesReadList.first().id)

            setMessagesReadStatusUseCase(
                messagesReadList,chatId,userId, onSuccess = { clearMessagesReadList() }
            )
        }
    }

    private fun clearMessagesReadList() = viewModelScope.launch {
        messagesReadList.clear()
    }
    private fun addMessageReadList(message: Message,userId: String) = viewModelScope.launch {
        if(!message.seenBy.contains(userId) && !messagesReadList.map { it.id }.contains(message.id)) {
            val seenBy = message.seenBy
            seenBy.add(userId)
            messagesReadList.add(message.copy(seenBy = seenBy))
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