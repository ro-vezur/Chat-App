package com.example.chatapp.model.db.userDbUsecases

import com.example.chatapp.USERS_DB_COLLECTION
import com.example.chatapp.model.db.userDbUsecases.gets.FindUsersByNameUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetCurrentUserIdUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetUserUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetUsersListWithIdsUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.AcceptFriendRequestUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.AddUserUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.DeclineFriendRequestUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.SendFriendRequestUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.fcmTokenUsecases.RemoveLastUserTokenUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.fcmTokenUsecases.UpdateCurrentUserTokenUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UsersDbUsecasesDI {

    @Provides
    @Singleton
    fun provideDeclineFriendRequestUseCase(
        db: FirebaseFirestore,
        getCurrentUserIdUseCase: GetCurrentUserIdUseCase
    ): DeclineFriendRequestUseCase {
        return DeclineFriendRequestUseCase(db, getCurrentUserIdUseCase)
    }

    @Provides
    @Singleton
    fun provideAcceptFriendRequestUseCase(db: FirebaseFirestore) = AcceptFriendRequestUseCase(db)

    @Provides
    @Singleton
    fun provideRemoveLastUserTokenUseCase(
        db: FirebaseFirestore,
    ): RemoveLastUserTokenUseCase = RemoveLastUserTokenUseCase(db)

    @Provides
    @Singleton
    fun provideUpdateCurrentUserTokenUseCase(
        getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
        db: FirebaseFirestore,
    ): UpdateCurrentUserTokenUseCase {
        return UpdateCurrentUserTokenUseCase(getCurrentUserIdUseCase, db,)
    }

    @Provides
    @Singleton
    fun provideCurrentUserUseCase(db: FirebaseFirestore): CollectionReference {
        return db.collection(USERS_DB_COLLECTION)
    }

    @Provides
    @Singleton
    fun provideAddUserUseCase(db: FirebaseFirestore): AddUserUseCase {
        return AddUserUseCase(db = db)
    }

    @Provides
    @Singleton
    fun provideGetUserUseCase(db: FirebaseFirestore): GetUserUseCase {
        return GetUserUseCase(db = db)
    }

    @Provides
    @Singleton
    fun provideFindUsersByNameUseCase(db: FirebaseFirestore, getCurrentUserIdUseCase: GetCurrentUserIdUseCase): FindUsersByNameUseCase {
        return FindUsersByNameUseCase(db, getCurrentUserIdUseCase)
    }

    @Provides
    @Singleton
    fun provideGetUsersListWithIdsUseCase(db: FirebaseFirestore): GetUsersListWithIdsUseCase {
        return GetUsersListWithIdsUseCase(db = db)
    }

    @Provides
    @Singleton
    fun provideSendFriendRequestUseCase(db: FirebaseFirestore): SendFriendRequestUseCase {
        return SendFriendRequestUseCase(db = db)
    }

    @Provides
    @Singleton
    fun provideGetCurrentUserIdUseCase(auth: FirebaseAuth): GetCurrentUserIdUseCase {
        return GetCurrentUserIdUseCase(auth)
    }

}