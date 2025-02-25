package com.example.chatapp.layouts.sharedComponents.validation.validators

import com.example.chatapp.helpers.strings.containsDigit
import com.example.chatapp.helpers.strings.containsLowercase
import com.example.chatapp.helpers.strings.containsUppercase
import com.example.chatapp.layouts.sharedComponents.validation.ValidationResult
import javax.inject.Inject

class ValidatePassword @Inject constructor(

) {
    operator fun invoke(password: String): ValidationResult {
        return when {
            password.length < 8 -> { ValidationResult.Error(errorMessage = "Minimum 8 Letters") }
            !password.containsUppercase() -> { ValidationResult.Error(errorMessage = "Minimum 1 Uppercase") }
            !password.containsLowercase() -> { ValidationResult.Error(errorMessage = "Minimum 1 Lowercase") }
            !password.containsDigit() -> { ValidationResult.Error(errorMessage = "Minimum 1 Digit") }
            else -> { ValidationResult.Success }
        }
    }
}