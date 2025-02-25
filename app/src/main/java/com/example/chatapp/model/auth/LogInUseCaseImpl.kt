package com.example.chatapp.model.auth

import com.example.chatapp.Dtos.User
import com.example.chatapp.domain.auth.LogInUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.fcmTokenUsecases.UpdateCurrentUserTokenUseCase
import com.example.chatapp.others.ResourceResult
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LogInUseCaseImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val updateCurrentUserTokenUseCase: UpdateCurrentUserTokenUseCase,
): LogInUseCase {
    override operator fun invoke(user: User): Flow<ResourceResult<AuthResult>> = flow {
        emit(ResourceResult.Loading())

        val result = firebaseAuth.signInWithEmailAndPassword(user.email,user.password).await()
        val newToken = Firebase.messaging.token.await()

        updateCurrentUserTokenUseCase(newToken)

        emit(ResourceResult.Success(data = result))
    }.catch { e ->
        emit(ResourceResult.Error(message = e.message.toString()))
    }
}