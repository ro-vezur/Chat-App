package com.example.chatapp.model.db.messagesDbUseCases

import com.example.chatapp.model.db.messagesDbUseCases.gets.GetAllUserChatsUnseenMessagesCountUseCase
import com.example.chatapp.model.db.messagesDbUseCases.gets.GetChatMessageUseCase
import com.example.chatapp.model.db.messagesDbUseCases.gets.GetLastReadMessageIdUseCase
import com.example.chatapp.model.db.messagesDbUseCases.gets.GetUnseenMessagesCountUseCase
import com.example.chatapp.model.db.messagesDbUseCases.posts.AddMessageUseCase
import com.example.chatapp.model.db.messagesDbUseCases.posts.DeleteMessageUseCase
import com.example.chatapp.model.db.messagesDbUseCases.posts.EditMessageUseCase
import com.example.chatapp.model.db.messagesDbUseCases.posts.SetMessagesReadStatusUseCase
import com.example.chatapp.model.db.messagesDbUseCases.posts.UpdateUserLastSeenMessageIdUseCase
import com.google.firebase.database.DatabaseReference
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
    fun provideEditMessageUseCase(fireStore: FirebaseFirestore,db: DatabaseReference) = EditMessageUseCase(fireStore,db)

    @Provides
    @Singleton
    fun provideDeleteMessageUseCase(fireStore: FirebaseFirestore, db: DatabaseReference) = DeleteMessageUseCase(fireStore,db)

    @Provides
    @Singleton
    fun provideGetAllUserChatsUnseenMessagesCountUseCase(fireStore: FirebaseFirestore) = GetAllUserChatsUnseenMessagesCountUseCase(fireStore)

    @Provides
    @Singleton
    fun provideGetUnseenMessagesCountUseCase(
        db: DatabaseReference
    ) = GetUnseenMessagesCountUseCase(db)

    @Provides
    @Singleton
    fun provideUpdateUserLastSeenMessageIdUseCase(
        fireStore: FirebaseFirestore
    ) = UpdateUserLastSeenMessageIdUseCase(fireStore)

    @Provides
    @Singleton
    fun provideGetLastReadMessageIdUseCase(
        fireStore: FirebaseFirestore
    ): GetLastReadMessageIdUseCase = GetLastReadMessageIdUseCase(fireStore)

    @Provides
    @Singleton
    fun provideSetMessagesReadStatusUseCase(
        db: DatabaseReference,
    ): SetMessagesReadStatusUseCase = SetMessagesReadStatusUseCase(db)

    @Provides
    @Singleton
    fun provideGetMessageUseCase(db: DatabaseReference) = GetChatMessageUseCase(db)

    @Provides
    @Singleton
    fun provideAddMessageUseCase(fireStore: FirebaseFirestore,dbReference: DatabaseReference) = AddMessageUseCase(fireStore,dbReference)
}