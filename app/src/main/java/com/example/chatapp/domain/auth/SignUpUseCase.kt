package com.example.chatapp.domain.auth

import com.example.chatapp.Dtos.user.User
import com.example.chatapp.others.ResourceResult
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface SignUpUseCase {
    operator fun invoke(user: User): Flow<ResourceResult<AuthResult>>
}