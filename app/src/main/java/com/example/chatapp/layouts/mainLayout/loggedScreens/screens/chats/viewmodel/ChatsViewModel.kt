package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chats.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.Dtos.chat.LocalChatInfo
import com.example.chatapp.model.db.chatDb.usecases.gets.GetUserChatsUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetCurrentUserIdUseCase
import com.example.chatapp.others.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val getUserChatsUseCase: GetUserChatsUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
): ViewModel() {

    private val _navigationEvents = MutableSharedFlow<String>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    private val _chatsUiState: MutableStateFlow<ChatsUiState> = MutableStateFlow(ChatsUiState())
    val chatsUiState: StateFlow<ChatsUiState> = _chatsUiState.asStateFlow()

    fun dispatchEvent(event: ChatsViewModelEvent) = viewModelScope.launch {
        when(event) {
            is ChatsViewModelEvent.FetchUserChats -> fetchChats(event.localChats)
            is ChatsViewModelEvent.NavigateTo -> navigateTo(event.route)
        }
    }

    private fun navigateTo(route: String) = viewModelScope.launch {
        _navigationEvents.emit(route)
    }

    private fun fetchChats(localChats: List<LocalChatInfo> ) = viewModelScope.launch {
        getUserChatsUseCase(getCurrentUserIdUseCase(),localChats).collectLatest { chatResource ->
            when(chatResource) {
                is Resource.Error -> Log.e("Error adding user chats",chatResource.message.toString())
                is Resource.Loading -> Log.e("LOADING USER CHATS","LOADING")
                is Resource.Success -> {
                    _chatsUiState.update { currentState ->
                        currentState.copy(
                            chats = chatResource
                        )
                    }
                }
            }
        }
    }
}