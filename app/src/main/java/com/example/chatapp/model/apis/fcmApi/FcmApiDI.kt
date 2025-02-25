package com.example.chatapp.model.apis.fcmApi

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FcmApiDI {

    @Provides
    @Singleton
    fun providesFcmApiInterface(retrofit: Retrofit): FcmApiInterface {
        return retrofit.create(FcmApiInterface::class.java)
    }
}