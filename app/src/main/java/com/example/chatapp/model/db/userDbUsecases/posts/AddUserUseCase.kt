package com.example.chatapp.model.db.userDbUsecases.posts

import android.content.Context
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.Dtos.user.UserOnlineStatus
import com.example.chatapp.USERS_DB_COLLECTION
import com.example.chatapp.helpers.context.deviceId
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val context: Context,
    fireStore: FirebaseFirestore,
    private val db: DatabaseReference,
) {
    private val usersDb = fireStore.collection(USERS_DB_COLLECTION)

    suspend operator fun invoke(user: User) {
        try {

            val userDocument = usersDb.document(user.id).get().await()

            if (!userDocument.exists()) {
                usersDb.document(user.id).set(
                    user.copy(
                        onlineStatus = UserOnlineStatus(devices = mutableListOf(context.deviceId()))
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}