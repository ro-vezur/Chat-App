package com.example.chatapp.layouts.sharedComponents.validation.validators

import com.example.chatapp.layouts.sharedComponents.validation.ValidationResult
import javax.inject.Inject

class ValidateConfirmPassword @Inject constructor() {
    operator fun invoke(password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isBlank() -> { ValidationResult.Error("Field is Empty") }
            confirmPassword != password -> { ValidationResult.Error("Passwords mismatch") }
            else -> { ValidationResult.Success }
        }
    }
}