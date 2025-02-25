package com.example.chatapp.model.db.userDbUsecases.posts

import android.util.Log
import com.example.chatapp.Dtos.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.example.chatapp.model.db.userDbUsecases.gets.GetCurrentUserIdUseCase
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class DeclineFriendRequestUseCase @Inject constructor(
    private val db: FirebaseFirestore,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) {
    val usersDb = db.collection(USERS_DB_COLLECTION)

    operator fun invoke(requestUserId: String) {
        db.runTransaction { transaction ->
            val userDocumentRef = usersDb.document(getCurrentUserIdUseCase())
            val user = transaction[userDocumentRef].toObject(User::class.java)

            Log.d("request user id",requestUserId)
            Log.d("request user",user.toString())

            if(user != null) {
                val requests = user.requests

                if(requests.map { it.userId }.contains(requestUserId)) {
                    requests.removeAll { it.userId == requestUserId }
                    Log.d("updated main user requests",requests.toString())
                    transaction.update(userDocumentRef,"requests",requests)
                }
            }
        }
    }
}