package com.example.chatapp.model.db.userDbUsecases.posts.fcmTokenUsecases

import com.example.chatapp.Dtos.user.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.example.chatapp.model.db.userDbUsecases.gets.GetCurrentUserIdUseCase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import javax.inject.Inject

class AddFcmTokenUseCase @Inject constructor(
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val fireStore: FirebaseFirestore,
) {
    private val usersCollection = fireStore.collection(USERS_DB_COLLECTION)

    operator fun invoke(newToken: String) {
        try {
            fireStore.runTransaction { transaction ->
                val userRef = usersCollection.document(getCurrentUserIdUseCase())
                val user = transaction[userRef].toObject<User>()

                user?.let {
                    val fcmTokens = user.fcmTokens
                    if(fcmTokens.contains(newToken)) {
                        fcmTokens.remove(newToken)
                    }

                    fcmTokens.add(newToken)
                    transaction.update(userRef,"fcmTokens",fcmTokens)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}