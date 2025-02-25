package com.example.chatapp.domain.auth

import com.example.chatapp.others.ResourceResult
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface GoogleLogInUseCase {
    operator fun invoke(): Flow<ResourceResult<AuthResult>>
}