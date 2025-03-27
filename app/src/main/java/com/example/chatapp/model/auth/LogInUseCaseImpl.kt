package com.example.chatapp.model.auth

import com.example.chatapp.Dtos.user.User
import com.example.chatapp.domain.auth.LogInUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.fcmTokenUsecases.AddFcmTokenUseCase
import com.example.chatapp.others.Resource
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
    private val addFcmTokenUseCase: AddFcmTokenUseCase,
): LogInUseCase {
    override operator fun invoke(user: User): Flow<Resource<AuthResult>> = flow {
        emit(Resource.Loading())

        val result = firebaseAuth.signInWithEmailAndPassword(user.email,user.password).await()
        val newToken = Firebase.messaging.token.await()

        addFcmTokenUseCase(newToken)

        emit(Resource.Success(data = result))
    }.catch { e ->
        emit(Resource.Error(message = e.message.toString()))
    }
}