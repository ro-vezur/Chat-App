package com.example.chatapp.layouts.mainLayout.starterScreens.logInScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.DD_MM_YYYY_HH_MM
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.domain.auth.LogInUseCase
import com.example.chatapp.helpers.time.getDateFromMillis
import com.example.chatapp.layouts.sharedComponents.validation.ValidationResult
import com.example.chatapp.layouts.sharedComponents.validation.validators.email.ValidateEmailUseCase
import com.example.chatapp.layouts.sharedComponents.validation.validators.password.CheckIsPasswordMatchesUseCase
import com.example.chatapp.layouts.sharedComponents.validation.validators.password.GetUserLoginStateUseCase
import com.example.chatapp.layouts.sharedComponents.validation.validators.password.UpdateUserLogInStateUseCase
import com.example.chatapp.layouts.sharedComponents.validation.validators.password.ValidatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val logInUseCase: LogInUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val checkIsPasswordMatchesUseCase: CheckIsPasswordMatchesUseCase,
    private val getUserLoginStateUseCase: GetUserLoginStateUseCase,
    private val updateUserLogInStateUseCase: UpdateUserLogInStateUseCase,
): ViewModel() {

    private val _logInUiState: MutableStateFlow<LogInUiState> = MutableStateFlow(LogInUiState())
    val logInUiState: StateFlow<LogInUiState> = _logInUiState.asStateFlow()

    fun updateEmail(
        email: String
    ) = viewModelScope.launch {

        _logInUiState.emit(
            _logInUiState.value.copy(
                user = _logInUiState.value.user.copy(email = email),
                emailValidationResult = validateEmailUseCase(
                    email = email,
                    checkIsEmailRegisteredValidator = false
                )
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

        updateUserLogInStateUseCase(user.email,user.password)

        val userLoginState = getUserLoginStateUseCase(user.email)

        when {
            userLoginState == null -> {
                _logInUiState.emit(
                    _logInUiState.value.copy(
                        passwordValidationResult = ValidationResult.Error(errorMessage = "Didn't Found User LogIn State")
                    )
                )
            }
            userLoginState.blockedUntil != null -> {
                _logInUiState.emit(
                    _logInUiState.value.copy(
                        passwordValidationResult = ValidationResult.Error(
                            errorMessage = "Try Again After ${getDateFromMillis(userLoginState.blockedUntil, DD_MM_YYYY_HH_MM)}"
                        )
                    )
                )
            }
            !checkIsPasswordMatchesUseCase(user.email,user.password) -> {
                _logInUiState.emit(
                    _logInUiState.value.copy(
                        passwordValidationResult = ValidationResult.Error(errorMessage = "Passwords Doesn't Matches")
                    )
                )
            }
            else -> {
                logInUseCase(user).collectLatest { result ->
                    _logInUiState.emit(
                        _logInUiState.value.copy(
                            logInResult = result
                        )
                    )
                }
            }
        }
    }
}