package com.example.chatapp.model.db.userDbUsecases.posts

import com.example.chatapp.Dtos.user.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.example.chatapp.model.db.userDbUsecases.gets.GetCurrentUserIdUseCase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UpdateOnlineStatusUseCase @Inject constructor(
    private val db: FirebaseFirestore,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
) {
    private val usersDb = db.collection(USERS_DB_COLLECTION)

    suspend operator fun invoke(status: Boolean) {
        val token = Firebase.messaging.token.await()

        db.runTransaction { transaction ->
            val userDocumentRef = usersDb.document(getCurrentUserIdUseCase())
            val user = transaction[userDocumentRef].toObject(User::class.java)

            when {
                user == null -> return@runTransaction
                else -> {
                    val fcmTokens = user.fcmTokens
                    fcmTokens[token] = status
                    transaction.update(userDocumentRef,"fcmTokens",fcmTokens)
                }
            }
        }.await()
    }
}