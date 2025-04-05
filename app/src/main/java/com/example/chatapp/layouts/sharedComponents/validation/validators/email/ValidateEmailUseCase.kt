package com.example.chatapp.layouts.sharedComponents.validation.validators.email

import android.util.Patterns
import com.example.chatapp.layouts.sharedComponents.validation.ValidationResult
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor(
    private val checkIsEmailRegisteredUseCase: CheckIsEmailRegisteredUseCase
) {
    suspend operator fun invoke(email: String, checkIsEmailRegisteredValidator: Boolean): ValidationResult {
        return when {
            email.isBlank() -> { ValidationResult.Error("Field is Empty") }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> { ValidationResult.Error("Incorrect Email Format") }
            checkIsEmailRegisteredUseCase(email) == checkIsEmailRegisteredValidator -> { ValidationResult.Error(
                if(checkIsEmailRegisteredValidator) "This Email Already Used" else "Didn't Found User With Given Email"
            )}
            else -> { ValidationResult.Success() }
        }
    }
}