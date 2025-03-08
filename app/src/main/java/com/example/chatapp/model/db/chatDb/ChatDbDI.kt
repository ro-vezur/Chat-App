package com.example.chatapp.model.db.chatDb

import com.example.chatapp.model.db.chatDb.observers.ObserveChatUseCase
import com.example.chatapp.model.db.chatDb.usecases.gets.GetOneToOneChatUseCase
import com.example.chatapp.model.db.chatDb.usecases.gets.GetUserChatsUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatDbDI {

    @Provides
    @Singleton
    fun provideChatPagingRepository(db: FirebaseFirestore) = ChatPagingRepository(db)

    @Provides
    @Singleton
    fun provideObserveChatUseCase(db: FirebaseFirestore) = ObserveChatUseCase(db)

    @Provides
    @Singleton
    fun provideGetOneToOneChat(db: FirebaseFirestore) = GetOneToOneChatUseCase(db)

    @Provides
    @Singleton
    fun provideGetUserChatsUseCase(db: FirebaseFirestore) = GetUserChatsUseCase(db)

}