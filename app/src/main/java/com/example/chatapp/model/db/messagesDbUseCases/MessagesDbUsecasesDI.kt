package com.example.chatapp.model.db.messagesDbUseCases

import com.example.chatapp.model.db.messagesDbUseCases.gets.GetLastChatMessageUseCase
import com.example.chatapp.model.db.messagesDbUseCases.gets.GetPaginatedChatMessagesUseCase
import com.example.chatapp.model.db.messagesDbUseCases.posts.AddMessageUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MessagesDbUsecasesDI {

    @Provides
    @Singleton
    fun provideGetLastChatMessage(db: FirebaseFirestore) = GetLastChatMessageUseCase(db)

    @Provides
    @Singleton
    fun provideGetPaginatedChatMessagesUseCase(db: FirebaseFirestore) = GetPaginatedChatMessagesUseCase(db)

    @Provides
    @Singleton
    fun provideAddMessageUseCase(db: FirebaseFirestore) = AddMessageUseCase(db)
}