package com.example.chatapp.model.db.userDbUsecases

import android.content.Context
import com.example.chatapp.USERS_DB_COLLECTION
import com.example.chatapp.model.db.userDbUsecases.gets.FindUsersByNameUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetCurrentUserIdUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetUserUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetUsersListWithIdsUseCase
import com.example.chatapp.model.db.userDbUsecases.observers.ObserveUserUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.AddUserUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.DeleteFriendUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.fcmTokenUsecases.AddFcmTokenUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.fcmTokenUsecases.RemoveFcmTokenUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.friendRequest.AcceptFriendRequestUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.friendRequest.DeclineFriendRequestUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.friendRequest.SendFriendRequestUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.userOnlineStatus.AddUserDeviceUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.userOnlineStatus.DeleteUserDeviceUseCase
import com.example.chatapp.model.services.messanging.SendRemoteNotificationUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UsersDbUsecasesDI {

    @Provides
    @Singleton
    fun provideDeleteUserDeviceUseCase(
        @ApplicationContext context: Context,
        fireStore: FirebaseFirestore,
    ): DeleteUserDeviceUseCase = DeleteUserDeviceUseCase(context,fireStore)

    @Provides
    @Singleton
    fun provideAddUserDeviceUseCase(
        @ApplicationContext context: Context,
        fireStore: FirebaseFirestore,
        getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    ): AddUserDeviceUseCase = AddUserDeviceUseCase(context,fireStore,getCurrentUserIdUseCase)

    @Provides
    @Singleton
    fun provideObserveUserUseCase(fireStore: FirebaseFirestore): ObserveUserUseCase {
        return ObserveUserUseCase(fireStore)
    }

    @Provides
    @Singleton
    fun provideDeleteFriendUseCase(
        fireStore: FirebaseFirestore,
        getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
        sendRemoteNotificationUseCase: SendRemoteNotificationUseCase,
        getUserUseCase: GetUserUseCase
    ): DeleteFriendUseCase {
        return DeleteFriendUseCase(
            db = fireStore,
            getCurrentUserIdUseCase = getCurrentUserIdUseCase,
            sendRemoteNotificationUseCase = sendRemoteNotificationUseCase,
            getUserUseCase = getUserUseCase,
        )
    }

    @Provides
    @Singleton
    fun provideDeclineFriendRequestUseCase(
        fireStore: FirebaseFirestore,
        getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
        sendRemoteNotificationUseCase: SendRemoteNotificationUseCase,
        getUserUseCase: GetUserUseCase
    ): DeclineFriendRequestUseCase {
        return DeclineFriendRequestUseCase(
            db = fireStore,
            getCurrentUserIdUseCase = getCurrentUserIdUseCase,
            sendRemoteNotificationUseCase = sendRemoteNotificationUseCase,
            getUserUseCase = getUserUseCase
        )
    }

    @Provides
    @Singleton
    fun provideAcceptFriendRequestUseCase(
        fireStore: FirebaseFirestore,
        sendRemoteNotificationUseCase: SendRemoteNotificationUseCase,
        getUserUseCase: GetUserUseCase
    ): AcceptFriendRequestUseCase {
        return AcceptFriendRequestUseCase(fireStore,sendRemoteNotificationUseCase,getUserUseCase)
    }

    @Provides
    @Singleton
    fun provideRemoveLastUserTokenUseCase(
        fireStore: FirebaseFirestore,
    ): RemoveFcmTokenUseCase = RemoveFcmTokenUseCase(fireStore)

    @Provides
    @Singleton
    fun provideUpdateCurrentUserTokenUseCase(
        getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
        fireStore: FirebaseFirestore,
    ): AddFcmTokenUseCase {
        return AddFcmTokenUseCase(getCurrentUserIdUseCase, fireStore)
    }

    @Provides
    @Singleton
    fun provideCurrentUserUseCase(fireStore: FirebaseFirestore): CollectionReference {
        return fireStore.collection(USERS_DB_COLLECTION)
    }

    @Provides
    @Singleton
    fun provideAddUserUseCase(
        @ApplicationContext context: Context,
        fireStore: FirebaseFirestore,
        db: DatabaseReference
    ): AddUserUseCase {
        return AddUserUseCase(context,fireStore,db)
    }

    @Provides
    @Singleton
    fun provideGetUserUseCase(fireStore: FirebaseFirestore): GetUserUseCase {
        return GetUserUseCase(fireStore)
    }

    @Provides
    @Singleton
    fun provideFindUsersByNameUseCase(fireStore: FirebaseFirestore, getCurrentUserIdUseCase: GetCurrentUserIdUseCase): FindUsersByNameUseCase {
        return FindUsersByNameUseCase(fireStore, getCurrentUserIdUseCase)
    }

    @Provides
    @Singleton
    fun provideGetUsersListWithIdsUseCase(fireStore: FirebaseFirestore): GetUsersListWithIdsUseCase {
        return GetUsersListWithIdsUseCase(fireStore)
    }

    @Provides
    @Singleton
    fun provideSendFriendRequestUseCase(
        fireStore: FirebaseFirestore,
        sendRemoteNotificationUseCase: SendRemoteNotificationUseCase
    ): SendFriendRequestUseCase {
        return SendFriendRequestUseCase(fireStore,sendRemoteNotificationUseCase)
    }

    @Provides
    @Singleton
    fun provideGetCurrentUserIdUseCase(auth: FirebaseAuth): GetCurrentUserIdUseCase {
        return GetCurrentUserIdUseCase(auth)
    }

}