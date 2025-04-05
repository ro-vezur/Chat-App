package com.example.chatapp.layouts.sharedComponents.validation.validators

import com.example.chatapp.layouts.sharedComponents.validation.ValidationResult
import javax.inject.Inject

class ValidateName @Inject constructor() {
    operator fun invoke(name: String): ValidationResult {
        return when {
            name.length < 3 -> { ValidationResult.Error("Minimum 3 letters") }
            name.length > 20 -> { ValidationResult.Error("Maximum 20 letters, currently ${name.length} letters used") }
            else -> { ValidationResult.Success() }
        }
    }
}