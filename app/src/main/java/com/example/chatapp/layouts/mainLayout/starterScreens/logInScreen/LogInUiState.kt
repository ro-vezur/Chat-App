package com.example.chatapp.layouts.mainLayout.starterScreens.logInScreen

import com.example.chatapp.Dtos.user.User
import com.example.chatapp.layouts.sharedComponents.validation.ValidationResult
import com.example.chatapp.others.Resource
import com.google.firebase.auth.AuthResult

data class LogInUiState(
    val user: User = User(),
    val emailValidationResult: ValidationResult = ValidationResult.None,
    val passwordValidationResult: ValidationResult = ValidationResult.None,
    val logInResult: Resource<AuthResult> = Resource.Loading()
)