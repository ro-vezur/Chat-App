package com.example.chatapp.model.auth

import com.example.chatapp.Dtos.user.User
import com.example.chatapp.domain.auth.SignUpUseCase
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

class SignUpUseCaseImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val updateCurrentUserTokenUseCase: UpdateCurrentUserTokenUseCase,
): SignUpUseCase {

    override operator fun invoke(user: User): Flow<ResourceResult<AuthResult>> = flow {
        emit(ResourceResult.Loading())

        val taskResult = firebaseAuth.createUserWithEmailAndPassword(user.email,user.password).await()
        val newToken = Firebase.messaging.token.await()

        updateCurrentUserTokenUseCase(newToken)

        emit(ResourceResult.Success(data = taskResult))
    }.catch { e ->
        emit(ResourceResult.Error(message = e.message.toString()))
    }

}