package com.example.chatapp.model.auth

import android.content.Context
import com.example.chatapp.domain.auth.GoogleLogInUseCase
import com.example.chatapp.domain.auth.LogInUseCase
import com.example.chatapp.domain.auth.SignUpUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.AddUserUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.fcmTokenUsecases.UpdateCurrentUserTokenUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthDI {

    @Provides
    @Singleton
    fun provideSignUpUseCase(
        firebaseAuth: FirebaseAuth,
        updateCurrentUserTokenUseCase: UpdateCurrentUserTokenUseCase,
    ): SignUpUseCase {
        return SignUpUseCaseImpl(firebaseAuth = firebaseAuth,updateCurrentUserTokenUseCase)
    }

    @Provides
    @Singleton
    fun provideLogInUseCase(
        firebaseAuth: FirebaseAuth,
        updateCurrentUserTokenUseCase: UpdateCurrentUserTokenUseCase,
    ): LogInUseCase {
        return LogInUseCaseImpl(firebaseAuth = firebaseAuth,updateCurrentUserTokenUseCase = updateCurrentUserTokenUseCase)
    }

    @Provides
    @Singleton
    fun provideGoogleLogIn(
        firebaseAuth: FirebaseAuth,
        @ApplicationContext context: Context,
        addUserUseCase: AddUserUseCase
    ): GoogleLogInUseCase {
        return GoogleLogInUseCaseImpl(
            firebaseAuth = firebaseAuth,
            context = context,
            addUserUseCase = addUserUseCase
        )
    }

}