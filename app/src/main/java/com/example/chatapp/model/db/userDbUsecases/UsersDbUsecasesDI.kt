package com.example.chatapp.model.db.userDbUsecases

import com.example.chatapp.USERS_DB_COLLECTION
import com.example.chatapp.model.db.userDbUsecases.gets.FindUsersByNameUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetCurrentUserIdUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetUserUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetUsersListWithIdsUseCase
import com.example.chatapp.model.db.userDbUsecases.observers.ObserveUserUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.AddUserUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.DeleteFriendUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.SetLastTimeSeenUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.UpdateOnlineStatusUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.fcmTokenUsecases.AddFcmTokenUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.fcmTokenUsecases.RemoveFcmTokenUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.friendRequest.AcceptFriendRequestUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.friendRequest.DeclineFriendRequestUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.friendRequest.SendFriendRequestUseCase
import com.example.chatapp.model.services.messanging.SendRemoteNotificationUseCase
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
    fun provideSetLastTimeSeenUseCase(
        db: FirebaseFirestore,
        getCurrentUserIdUseCase: GetCurrentUserIdUseCase
    ): SetLastTimeSeenUseCase {
        return SetLastTimeSeenUseCase(db,getCurrentUserIdUseCase)
    }

    @Provides
    @Singleton
    fun provideUpdateOnlineStatuesUseCase(
        db: FirebaseFirestore,
        getCurrentUserIdUseCase: GetCurrentUserIdUseCase
    ): UpdateOnlineStatusUseCase {
        return UpdateOnlineStatusUseCase(db,getCurrentUserIdUseCase)
    }

    @Provides
    @Singleton
    fun provideObserveUserUseCase(db: FirebaseFirestore): ObserveUserUseCase {
        return ObserveUserUseCase(db)
    }

    @Provides
    @Singleton
    fun provideDeleteFriendUseCase(
        db: FirebaseFirestore,
        getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
        sendRemoteNotificationUseCase: SendRemoteNotificationUseCase,
        getUserUseCase: GetUserUseCase
    ): DeleteFriendUseCase {
        return DeleteFriendUseCase(
            db = db,
            getCurrentUserIdUseCase = getCurrentUserIdUseCase,
            sendRemoteNotificationUseCase = sendRemoteNotificationUseCase,
            getUserUseCase = getUserUseCase,
        )
    }

    @Provides
    @Singleton
    fun provideDeclineFriendRequestUseCase(
        db: FirebaseFirestore,
        getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
        sendRemoteNotificationUseCase: SendRemoteNotificationUseCase,
        getUserUseCase: GetUserUseCase
    ): DeclineFriendRequestUseCase {
        return DeclineFriendRequestUseCase(
            db = db,
            getCurrentUserIdUseCase = getCurrentUserIdUseCase,
            sendRemoteNotificationUseCase = sendRemoteNotificationUseCase,
            getUserUseCase = getUserUseCase
        )
    }

    @Provides
    @Singleton
    fun provideAcceptFriendRequestUseCase(
        db: FirebaseFirestore,
        sendRemoteNotificationUseCase: SendRemoteNotificationUseCase,
        getUserUseCase: GetUserUseCase
    ): AcceptFriendRequestUseCase {
        return AcceptFriendRequestUseCase(db,sendRemoteNotificationUseCase,getUserUseCase)
    }

    @Provides
    @Singleton
    fun provideRemoveLastUserTokenUseCase(
        db: FirebaseFirestore,
    ): RemoveFcmTokenUseCase = RemoveFcmTokenUseCase(db)

    @Provides
    @Singleton
    fun provideUpdateCurrentUserTokenUseCase(
        getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
        db: FirebaseFirestore,
    ): AddFcmTokenUseCase {
        return AddFcmTokenUseCase(getCurrentUserIdUseCase, db,)
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
    fun provideSendFriendRequestUseCase(
        db: FirebaseFirestore,
        sendRemoteNotificationUseCase: SendRemoteNotificationUseCase
    ): SendFriendRequestUseCase {
        return SendFriendRequestUseCase(db = db,sendRemoteNotificationUseCase)
    }

    @Provides
    @Singleton
    fun provideGetCurrentUserIdUseCase(auth: FirebaseAuth): GetCurrentUserIdUseCase {
        return GetCurrentUserIdUseCase(auth)
    }

}