package com.example.chatapp.model.db.userDbUsecases.posts.fcmTokenUsecases

import android.util.Log
import com.example.chatapp.Dtos.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class RemoveLastUserTokenUseCase @Inject constructor(
    private val db: FirebaseFirestore,
) {
    private val usersDb = db.collection(USERS_DB_COLLECTION)

    operator fun invoke(lastUserId: String, token: String) {

        Log.d("last user id",lastUserId)
        Log.d("token",token)

        db.runTransaction { transaction ->
            val userDocumentRef = usersDb.document(lastUserId)
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