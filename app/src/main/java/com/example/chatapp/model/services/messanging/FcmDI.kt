package com.example.chatapp.model.services.messanging

import com.example.chatapp.model.apis.fcmApi.FcmApiInterface
import com.example.chatapp.model.db.userDbUsecases.posts.fcmTokenUsecases.RemoveFcmTokenUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FcmDI {

    @Provides
    @Singleton
    fun providesSendRemoteNotificationUseCase(
        fcmApiInterface: FcmApiInterface,
        removeFcmTokenUseCase: RemoveFcmTokenUseCase
    ): SendRemoteNotificationUseCase {
        return SendRemoteNotificationUseCase(fcmApiInterface,removeFcmTokenUseCase)
    }
}