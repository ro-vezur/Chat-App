package com.example.chatapp.layouts.mainLayout.starterScreens.signUpScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.domain.auth.SignUpUseCase
import com.example.chatapp.layouts.sharedComponents.validation.validators.ValidateConfirmPassword
import com.example.chatapp.layouts.sharedComponents.validation.validators.ValidateName
import com.example.chatapp.layouts.sharedComponents.validation.validators.email.ValidateEmailUseCase
import com.example.chatapp.layouts.sharedComponents.validation.validators.password.ValidatePassword
import com.example.chatapp.model.db.userDbUsecases.posts.AddUserUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.fcmTokenUsecases.AddFcmTokenUseCase
import com.example.chatapp.others.Resource
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val addUserUseCase: AddUserUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val addFcmTokenUseCase: AddFcmTokenUseCase,
): ViewModel() {

    private val _signUpUiState: MutableStateFlow<SignUpUiState> = MutableStateFlow(SignUpUiState())
    val signUpUiState = _signUpUiState.asStateFlow()

    fun updateName(
        name: String
    ) = viewModelScope.launch {
        val nameValidationResult = ValidateName()

        _signUpUiState.emit(
            _signUpUiState.value.copy(
                user = _signUpUiState.value.user.copy(name = name),
                nameValidationResult = nameValidationResult(name)
            )
        )
    }

    fun updateEmail(
        email: String
    ) = viewModelScope.launch {

        _signUpUiState.emit(
            _signUpUiState.value.copy(
                user = _signUpUiState.value.user.copy(email = email),
                emailValidationResult = validateEmailUseCase(
                    email =  email,
                    checkIsEmailRegisteredValidator = true
                )
            )
        )
    }

    fun updatePassword(
        password: String
    ) = viewModelScope.launch {
        val passwordValidationResult = ValidatePassword()

        _signUpUiState.emit(
            _signUpUiState.value.copy(
                user = _signUpUiState.value.user.copy(password = password),
                passwordValidationResult = passwordValidationResult(password)
            )
        )
    }

    fun updateConfirmPassword(
        password: String,
        confirmPassword: String,
    ) = viewModelScope.launch {
        val confirmPasswordResult = ValidateConfirmPassword()

        _signUpUiState.emit(
            _signUpUiState.value.copy(
                passwordConfirm = confirmPassword,
                passwordConfirmValidationResult = confirmPasswordResult(password,confirmPassword)
            )
        )
    }

    fun signUp(user: User) = viewModelScope.launch {
        signUpUseCase(user).collectLatest { result ->
            Log.d("result",result.toString())
            when(result) {
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                 //   Log.d("user success!",user.copy(id = result.data?.user?.uid.toString()).toString())
                    addUserUseCase(user.copy(id = result.data?.user?.uid.toString()))

                    val newToken = Firebase.messaging.token.await()
                    addFcmTokenUseCase(newToken)
                }
            }
        }
    }
}