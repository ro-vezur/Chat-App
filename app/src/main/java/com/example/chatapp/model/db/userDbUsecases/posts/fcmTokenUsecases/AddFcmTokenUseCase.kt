package com.example.chatapp.model.db.userDbUsecases.posts.fcmTokenUsecases

import android.util.Log
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.example.chatapp.model.db.userDbUsecases.gets.GetCurrentUserIdUseCase
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AddFcmTokenUseCase @Inject constructor(
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val db: FirebaseFirestore,
) {

    private val usersDb = db.collection(USERS_DB_COLLECTION)

    operator fun invoke(newToken: String) {
        db.runTransaction { transaction ->
            val userDocumentRef = usersDb.document(getCurrentUserIdUseCase())
            val user = transaction.get(userDocumentRef).toObject(User::class.java)

            Log.d("user",user.toString())

            when {
                user == null -> return@runTransaction
                else -> {
                    val tokens = user.fcmTokens

                    if(!tokens.contains(newToken)) {
                        tokens[newToken] = true
                        transaction.update(userDocumentRef,"fcmTokens",tokens)
                    }
                }
            }
        }
    }
}