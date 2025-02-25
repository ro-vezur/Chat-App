package com.example.chatapp.model.db.userDbUsecases.gets

import com.example.chatapp.Dtos.User
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getUser: GetUserUseCase,
) {
    suspend operator fun invoke(): User {
        return try {
            return getUser(getCurrentUserIdUseCase())
        } catch (e: Exception) {
            User()
        }
    }
}