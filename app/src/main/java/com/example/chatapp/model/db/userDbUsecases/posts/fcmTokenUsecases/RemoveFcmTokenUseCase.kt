package com.example.chatapp.model.db.userDbUsecases.posts.fcmTokenUsecases

import com.example.chatapp.Dtos.user.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoveFcmTokenUseCase @Inject constructor(
    private val fireStore: FirebaseFirestore,
) {
    private val usersCollection = fireStore.collection(USERS_DB_COLLECTION)

    suspend operator fun invoke(userId: String, token: String,onSuccess: () -> Unit = {} ) {
        try {
            fireStore.runTransaction { transaction ->
                val userRef = usersCollection.document(userId)
                val user = transaction[userRef].toObject<User>()

                user?.let {
                    val fcmTokens = user.fcmTokens

                    if(fcmTokens.contains(token)) {
                        fcmTokens.remove(token)
                        transaction.update(userRef,"fcmTokens",fcmTokens)
                    }
                }
            }.await()

            onSuccess()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}