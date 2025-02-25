package com.example.chatapp.layouts.sharedComponents.validation

sealed class ValidationResult(val errorMessage: String? = null) {
    data object Success: ValidationResult()
    class Error(errorMessage: String): ValidationResult(errorMessage)
    data object None: ValidationResult()
}