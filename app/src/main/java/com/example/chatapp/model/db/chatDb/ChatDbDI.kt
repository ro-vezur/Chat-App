package com.example.chatapp.model.db.chatDb

import com.example.chatapp.model.db.chatDb.observers.ObserveChatUseCase
import com.example.chatapp.model.db.chatDb.observers.ObserveTypingUsersUseCase
import com.example.chatapp.model.db.chatDb.usecases.gets.GetOneToOneChatUseCase
import com.example.chatapp.model.db.chatDb.usecases.gets.GetUserChatsUseCase
import com.example.chatapp.model.db.chatDb.usecases.posts.usersTyping.AddUserTypingUseCase
import com.example.chatapp.model.db.chatDb.usecases.posts.usersTyping.RemoveUserTypingUseCase
import com.example.chatapp.model.db.messagesDbUseCases.gets.GetChatMessageUseCase
import com.example.chatapp.model.db.messagesDbUseCases.gets.GetLastReadMessageIdUseCase
import com.example.chatapp.model.db.messagesDbUseCases.gets.GetUnseenMessagesCountUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetCurrentUserIdUseCase
import com.example.chatapp.model.db.userDbUsecases.observers.ObserveUserUseCase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ChatDbDI {

    @Provides
    @ViewModelScoped
    fun provideObserveTypingUsersUseCase(
        db: DatabaseReference
    ) = ObserveTypingUsersUseCase(db)

    @Provides
    @ViewModelScoped
    fun provideRemoveUserTypingUseCase(
        db: DatabaseReference
    ) = RemoveUserTypingUseCase(db)

    @Provides
    @ViewModelScoped
    fun provideAddUserTypingUseCase(
        db: DatabaseReference
    ) = AddUserTypingUseCase(db)

    @Provides
    @ViewModelScoped
    fun provideChatPagingRepository(
        db: DatabaseReference,
        getLastReadMessageIdUseCase: GetLastReadMessageIdUseCase,
        getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
        getChatMessageUseCase: GetChatMessageUseCase
    ) = ChatPagingRepository(db,getLastReadMessageIdUseCase, getCurrentUserIdUseCase, getChatMessageUseCase)

    @Provides
    @ViewModelScoped
    fun provideObserveChatUseCase(db: FirebaseFirestore) = ObserveChatUseCase(db)

    @Provides
    @ViewModelScoped
    fun provideGetOneToOneChat(db: FirebaseFirestore) = GetOneToOneChatUseCase(db)

    @Provides
    @ViewModelScoped
    fun provideGetUserChatsUseCase(
        db: FirebaseFirestore,
        observeUserUseCase: ObserveUserUseCase,
        getChatMessageUseCase: GetChatMessageUseCase,
        getUnseenMessagesCountUseCase: GetUnseenMessagesCountUseCase,
    ) = GetUserChatsUseCase(db,observeUserUseCase,getChatMessageUseCase,getUnseenMessagesCountUseCase)

}