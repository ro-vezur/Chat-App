package com.example.chatapp.domain.auth

import com.example.chatapp.Dtos.user.User
import com.example.chatapp.others.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface LogInUseCase {
    operator fun invoke(user: User): Flow<Resource<AuthResult>>
}