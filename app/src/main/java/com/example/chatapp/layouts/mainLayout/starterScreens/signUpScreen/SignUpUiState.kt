package com.example.chatapp.layouts.mainLayout.starterScreens.signUpScreen

import com.example.chatapp.Dtos.user.User
import com.example.chatapp.layouts.sharedComponents.validation.ValidationResult
import com.example.chatapp.others.ResourceResult
import com.google.firebase.auth.AuthResult

data class SignUpUiState(
    val user: User = User(),
    val passwordConfirm: String = "",
    val nameValidationResult: ValidationResult = ValidationResult.None,
    val emailValidationResult: ValidationResult = ValidationResult.None,
    val passwordValidationResult: ValidationResult = ValidationResult.None,
    val passwordConfirmValidationResult: ValidationResult = ValidationResult.None,
    val signUpResult: ResourceResult<AuthResult> = ResourceResult.Loading()
)