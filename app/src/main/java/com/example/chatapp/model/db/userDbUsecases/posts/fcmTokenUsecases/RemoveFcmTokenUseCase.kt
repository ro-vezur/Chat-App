package com.example.chatapp.model.db.userDbUsecases.posts.fcmTokenUsecases

import com.example.chatapp.Dtos.user.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class RemoveFcmTokenUseCase @Inject constructor(
    private val db: FirebaseFirestore,
) {
    private val usersDb = db.collection(USERS_DB_COLLECTION)

    operator fun invoke(userId: String, token: String) {

        db.runTransaction { transaction ->
            val userDocumentRef = usersDb.document(userId)
            val user = transaction[userDocumentRef].toObject(User::class.java)

            if(user != null) {
                val tokens = user.fcmTokens

                if(tokens.contains(token)) {
                    tokens.remove(token)
                    transaction.update(userDocumentRef,"fcmTokens",tokens)
                }
            } else {
                return@runTransaction
            }
        }
    }
}