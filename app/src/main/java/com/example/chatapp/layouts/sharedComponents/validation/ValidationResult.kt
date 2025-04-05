package com.example.chatapp.layouts.sharedComponents.validation

sealed class ValidationResult(val resultMessage: String? = null) {
    class Success(successMessage: String? = null): ValidationResult(successMessage)
    class Error(errorMessage: String): ValidationResult(resultMessage = errorMessage)
    data object None: ValidationResult()
}