package com.example.chatapp

import com.example.chatapp.layouts.sharedComponents.validation.ValidationResult
import com.example.chatapp.layouts.sharedComponents.validation.validators.ValidateConfirmPassword
import com.example.chatapp.layouts.sharedComponents.validation.validators.email.ValidateEmailUseCase
import com.example.chatapp.layouts.sharedComponents.validation.validators.ValidateName
import com.example.chatapp.layouts.sharedComponents.validation.validators.password.ValidatePassword
import org.junit.Assert.assertTrue
import org.junit.Test

class SignUpValidationUnitTests {

    val nameValidation = ValidateName()
    val emailValidation = ValidateEmailUseCase()
    val passwordValidation = ValidatePassword()
    val passwordConfirmValidation = ValidateConfirmPassword()

    @Test
    fun isNameValid() {
        val name = "Roma"
        assertTrue(nameValidation(name) is ValidationResult.Success)
    }

    @Test
    fun isNameInvalid() {
        val name = "af"
        assertTrue(nameValidation(name) is ValidationResult.Error)
    }

    @Test
    fun isEmailValid() {
        val email = "example@gmail.com"
        assertTrue(emailValidation(email) is ValidationResult.Success)
    }

    @Test
    fun isPasswordValid() {
        val password = "Romapoma22"
        assertTrue(passwordValidation(password) is ValidationResult.Success)
    }

    @Test
    fun isPasswordConfirmValid() {
        val password = "Romapoma2"
        val passwordConfirm = "Romapoma2"
        assertTrue(passwordConfirmValidation(password,passwordConfirm) is ValidationResult.Success)
    }

}