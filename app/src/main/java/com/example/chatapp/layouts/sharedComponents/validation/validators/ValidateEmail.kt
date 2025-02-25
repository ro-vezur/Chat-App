package com.example.chatapp.layouts.sharedComponents.validation.validators

import android.util.Patterns
import com.example.chatapp.layouts.sharedComponents.validation.ValidationResult
import javax.inject.Inject

class ValidateEmail @Inject constructor() {
    operator fun invoke(email: String): ValidationResult {
        return when {
            email.isBlank() -> { ValidationResult.Error("Field is Empty") }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> { ValidationResult.Error("Incorrect Email Format") }
            else -> { ValidationResult.Success }
        }
    }
}