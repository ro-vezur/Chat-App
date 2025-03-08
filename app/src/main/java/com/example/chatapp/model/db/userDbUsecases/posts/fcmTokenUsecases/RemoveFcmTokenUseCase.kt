package com.example.chatapp.model.db.userDbUsecases.posts.fcmTokenUsecases

import com.example.chatapp.Dtos.user.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoveFcmTokenUseCase @Inject constructor(
    private val db: FirebaseFirestore,
) {
    private val usersDb = db.collection(USERS_DB_COLLECTION)
    private var isSuccess = false

    suspend operator fun invoke(userId: String, token: String,onSuccess: suspend () -> Unit = {} ) {

        db.runTransaction { transaction ->
            val userDocumentRef = usersDb.document(userId)
            val user = transaction[userDocumentRef].toObject(User::class.java)

            if(user != null) {
                val tokens = user.fcmTokens

                if(tokens.contains(token)) {
                    isSuccess = true
                    tokens.remove(token)
                    transaction.update(userDocumentRef,"fcmTokens",tokens)
                }
            } else {
                isSuccess = false
                return@runTransaction
            }
        }.await()

        if(isSuccess) {
            onSuccess()
        }
    }
}