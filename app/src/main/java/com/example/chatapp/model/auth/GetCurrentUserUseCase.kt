package com.example.chatapp.model.auth

import com.example.chatapp.Dtos.User
import com.example.chatapp.model.db.userDbUsecases.gets.GetUserUseCase
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val auth: FirebaseAuth,
    private val getUserUseCase: GetUserUseCase,
) {
    suspend operator fun invoke(): User {
        return getUserUseCase(auth.currentUser?.uid.toString())
    }
}