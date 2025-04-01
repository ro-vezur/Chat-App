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
import com.example.chatapp.helpers.time.getCurrentTimeInMillis
import com.example.chatapp.model.db.chatDb.ChatPagingRepository
import com.example.chatapp.model.db.chatDb.observers.ObserveChatUseCase
import com.example.chatapp.model.db.chatDb.observers.ObserveTypingUsersUseCase
import com.example.chatapp.model.db.chatDb.usecases.posts.usersTyping.AddUserTypingUseCase
import com.example.chatapp.model.db.chatDb.usecases.posts.usersTyping.RemoveUserTypingUseCase
import com.example.chatapp.model.db.messagesDbUseCases.gets.GetChatMessageUseCase
import com.example.chatapp.model.db.messagesDbUseCases.gets.GetUnseenMessagesCountUseCase
import com.example.chatapp.model.db.messagesDbUseCases.posts.AddMessageUseCase
import com.example.chatapp.model.db.messagesDbUseCases.posts.SetMessagesReadStatusUseCase
import com.example.chatapp.model.db.messagesDbUseCases.posts.UpdateUserLastSeenMessageIdUseCase
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
    @Assisted("oppositeUserId") private val oppositeUserId: String,
    private val observeUserUseCase: ObserveUserUseCase,
    private val observeChatUseCase: ObserveChatUseCase,
    private val addMessageUseCase: AddMessageUseCase,
    private val addLocalChatInfoUseCase: AddLocalChatInfoUseCase,
    private val chatPagingRepository: ChatPagingRepository,
    private val setMessagesReadStatusUseCase: SetMessagesReadStatusUseCase,
    private val updateUserLastSeenMessageIdUseCase: UpdateUserLastSeenMessageIdUseCase,
    private val getChatMessageUseCase: GetChatMessageUseCase,
    private val getUnseenMessagesCountUseCase: GetUnseenMessagesCountUseCase,
    private val addUserTypingUseCase: AddUserTypingUseCase,
    private val removeUserTypingUseCase: RemoveUserTypingUseCase,
    private val observeTypingUsersUseCase: ObserveTypingUsersUseCase
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("chatId") chatId: String,
            @Assisted("oppositeUserId") userId: String
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
            observeTypingUsers()
            chatPagingRepository.messagesListener(chatId)
        }
    }
    private fun observeUser() = viewModelScope.launch {
        observeUserUseCase(oppositeUserId).collectLatest { user ->
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

    private fun observeTypingUsers() = viewModelScope.launch {
        observeTypingUsersUseCase(chatId).collectLatest { users ->
            _chatUiState.update { state ->
                state.copy(usersTyping = users)
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
            is OneToOneChatViewModelEvent.SetUnseenMessagesCount -> getUnseenMessagesCount(event.userId)
            is OneToOneChatViewModelEvent.AddUserTyping -> addUserTyping(event.chatId,event.userId)
            is OneToOneChatViewModelEvent.RemoveUserTyping -> removeUserTyping(event.chatId,event.userId)
        }
    }

    private fun removeUserTyping(chatId: String,userId: String) = viewModelScope.launch {
        removeUserTypingUseCase(chatId, userId)
    }

    private fun addUserTyping(chatId: String,userId: String) = viewModelScope.launch {
        addUserTypingUseCase(chatId, userId)
    }

    private fun getUnseenMessagesCount(userId: String) = viewModelScope.launch {

        _chatUiState.update { state ->
            val lastReadMessage = getChatMessageUseCase(chatId,state.chat.lastReads[userId] ?: "")
            state.copy(
                unseenMessagesCount = getUnseenMessagesCountUseCase(chatId,lastReadMessage)
            )
        }
    }

    private fun setMessagesReadStatus(userId: String) = viewModelScope.launch {
        if(messagesReadList.isNotEmpty()) {
            val lastReadMessageId = _chatUiState.value.chat.lastReads[userId] ?: ""
            val lastReadMessage = getChatMessageUseCase(chatId,lastReadMessageId)

            messagesReadList.sortByDescending { it.sentTimeStamp }

            if((lastReadMessage.sentTimeStamp ?: 0) < (messagesReadList.first().sentTimeStamp ?: 0) || lastReadMessageId.isEmpty()) {

                updateUserLastSeenMessageIdUseCase(userId,chatId,messagesReadList.first().id)
            }

            setMessagesReadStatusUseCase(
                messagesReadList.filter { it.userId != userId },chatId,userId, onSuccess = { clearMessagesReadList() }
            )
        }
    }

    private fun clearMessagesReadList() = viewModelScope.launch {
        messagesReadList.clear()
    }
    private fun addMessageReadList(message: Message,userId: String) = viewModelScope.launch {
        if(!message.seenBy.contains(userId) && !messagesReadList.map { it.id }.contains(message.id)) {
            val seenBy = message.seenBy
            if(message.userId != userId) {
                seenBy[userId] = getCurrentTimeInMillis()
            }
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
            state.copy(sendMessageText = query)
        }
    }

}