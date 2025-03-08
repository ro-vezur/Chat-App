package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chats.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.Dtos.chat.LocalChatInfo
import com.example.chatapp.model.db.chatDb.usecases.gets.GetUserChatsUseCase
import com.example.chatapp.model.db.messagesDbUseCases.gets.GetLastChatMessageUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetCurrentUserIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val getUserChatsUseCase: GetUserChatsUseCase,
    private val getLastChatMessageUseCase: GetLastChatMessageUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
): ViewModel() {


    private val _chatsUiState: MutableStateFlow<ChatsUiState> = MutableStateFlow(ChatsUiState())
    val chatsUiState: StateFlow<ChatsUiState> = _chatsUiState.asStateFlow()

    init {
     //   dispatchEvent(ChatsViewModelEvent.FetchUserChats(getCurrentUserIdUseCase()))
    }

    fun dispatchEvent(event: ChatsViewModelEvent) = viewModelScope.launch {
        when(event) {
            is ChatsViewModelEvent.FetchUserChats -> fetchChats(event.localChats)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun fetchChats(localChats: List<LocalChatInfo> ) = viewModelScope.launch {
        getUserChatsUseCase(getCurrentUserIdUseCase(),localChats).collectLatest { chatResource ->
            chatResource.data = chatResource.data?.map {
                val lastMessage = getLastChatMessageUseCase(it.id)
                Log.d("last message",lastMessage)
                it.copy(lastMessage = lastMessage)
            } ?: emptyList()
            _chatsUiState.update { currentState ->
                currentState.copy(
                    chats = chatResource
                )
            }
        }
    }
}