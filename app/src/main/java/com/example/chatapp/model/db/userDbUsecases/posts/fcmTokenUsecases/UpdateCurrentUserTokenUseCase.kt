package com.example.chatapp.model.db.userDbUsecases.posts.fcmTokenUsecases

import android.util.Log
import com.example.chatapp.Dtos.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.example.chatapp.model.db.userDbUsecases.gets.GetCurrentUserIdUseCase
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class UpdateCurrentUserTokenUseCase @Inject constructor(
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val db: FirebaseFirestore,
) {

    private val usersDb = db.collection(USERS_DB_COLLECTION)

    operator fun invoke(newToken: String): Task<Unit> {
        return db.runTransaction { transaction ->
            val userDocumentRef = usersDb.document(getCurrentUserIdUseCase())
            val user = transaction.get(userDocumentRef).toObject(User::class.java)

            Log.d("user",user.toString())

            if(user != null) {
                val tokens = user.fcmTokens

                Log.d("newToken",newToken)

                if(!tokens.contains(newToken)) {
                    tokens.add(newToken)
                    transaction.update(userDocumentRef,"fcmTokens",tokens)
                } else {
                    tokens.remove(newToken)
                    transaction.update(userDocumentRef,"fcmTokens",tokens)
                }

            } else {
                return@runTransaction
            }
        }
    }
}