package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.settingsViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.Dtos.user.userSettings.UserSettings
import com.example.chatapp.model.db.userDbUsecases.gets.GetCurrentUserIdUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.UpdateUserSettingsUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.fcmTokenUsecases.RemoveFcmTokenUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.userOnlineStatus.DeleteUserDeviceUseCase
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
    private val removeTokenUseCase: RemoveFcmTokenUseCase,
    private val auth: FirebaseAuth,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val deleteUserDeviceUseCase: DeleteUserDeviceUseCase,
    private val updateUserSettingsUseCase: UpdateUserSettingsUseCase,
): ViewModel() {
    private val _settingsUiState: MutableStateFlow<SettingsUiState> = MutableStateFlow(
        SettingsUiState()
    )
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState.asStateFlow()

    fun dispatchEvent(event: SettingsViewModelEvent) = viewModelScope.launch {
        when(event) {
            SettingsViewModelEvent.LogOut -> logOut()
            is SettingsViewModelEvent.UpdateUserSettings -> updateUserSettings(event.userId,event.settings)
        }
    }

    private fun logOut() = viewModelScope.launch {
        val newToken = Firebase.messaging.token.await()
        val currentUserId = getCurrentUserIdUseCase()

        removeTokenUseCase(
            userId = currentUserId,
            token = newToken,
            onSuccess = {
            //    deleteUserDeviceUseCase(currentUserId)
                auth.signOut()
            }
        )
    }

    private fun updateUserSettings(userId: String,settings: UserSettings) = viewModelScope.launch {
        updateUserSettingsUseCase(userId,settings)
    }
}