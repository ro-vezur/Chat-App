package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.settingsViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.db.userDbUsecases.posts.fcmTokenUsecases.UpdateCurrentUserTokenUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val updateCurrentUserTokenUseCase: UpdateCurrentUserTokenUseCase,
    private val auth: FirebaseAuth,
): ViewModel() {
    private val _settingsUiState: MutableStateFlow<SettingsUiState> = MutableStateFlow(
        SettingsUiState()
    )
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState.asStateFlow()

    fun dispatchEvent(event: SettingsViewModelEvent) = viewModelScope.launch {
        when(event) {
            SettingsViewModelEvent.LogOut -> logOut()
        }
    }

    private fun logOut() = viewModelScope.launch {
        val newToken = Firebase.messaging.token.await()

        updateCurrentUserTokenUseCase(newToken).addOnSuccessListener {
            auth.signOut()
        }
    }

}