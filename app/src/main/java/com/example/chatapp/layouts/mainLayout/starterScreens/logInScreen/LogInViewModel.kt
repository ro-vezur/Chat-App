package com.example.chatapp.layouts.mainLayout.starterScreens.logInScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.Dtos.User
import com.example.chatapp.domain.auth.LogInUseCase
import com.example.chatapp.layouts.sharedComponents.validation.validators.ValidateEmail
import com.example.chatapp.layouts.sharedComponents.validation.validators.ValidatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val logInUseCase: LogInUseCase
): ViewModel() {

    private val _logInUiState: MutableStateFlow<LogInUiState> = MutableStateFlow(LogInUiState())
    val logInUiState: StateFlow<LogInUiState> = _logInUiState.asStateFlow()

    fun updateEmail(
        email: String
    ) = viewModelScope.launch {
        val emailValidationResult = ValidateEmail()

        _logInUiState.emit(
            _logInUiState.value.copy(
                user = _logInUiState.value.user.copy(email = email),
                emailValidationResult = emailValidationResult(email)
            )
        )
    }

    fun updatePassword(
        password: String
    ) = viewModelScope.launch {
        val passwordValidationResult = ValidatePassword()

        _logInUiState.emit(
            _logInUiState.value.copy(
                user = _logInUiState.value.user.copy(password = password),
                passwordValidationResult = passwordValidationResult(password)
            )
        )
    }

    fun logIn(user: User) = viewModelScope.launch {
        logInUseCase(user).collectLatest { result ->
            _logInUiState.emit(
                _logInUiState.value.copy(
                    logInResult = result
                )
            )
        }
    }

}