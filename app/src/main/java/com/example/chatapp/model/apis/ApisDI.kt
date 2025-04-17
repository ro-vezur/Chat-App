package com.example.chatapp.model.apis

import com.example.chatapp.BASE_API_URL
import com.example.chatapp.domain.MediaInterface
import com.example.chatapp.domain.apis.FcmApiInterface
import com.example.chatapp.domain.apis.MediaApiInterface
import com.example.chatapp.model.apis.apisUsecases.DeleteImageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApisDI {

    @Provides
    @Singleton
    fun providesDeleteImageUseCase(mediaApiInterface: MediaApiInterface) = DeleteImageUseCase(mediaApiInterface)

    @Provides
    @Singleton
    fun providesMediaInterface(): MediaInterface {
        return MediaImpl()
    }

    @Provides
    @Singleton
    fun provideMediaApiInterface(retrofit: Retrofit): MediaApiInterface {
        return retrofit.create(MediaApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun providesFcmApiInterface(retrofit: Retrofit): FcmApiInterface {
        return retrofit.create(FcmApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}