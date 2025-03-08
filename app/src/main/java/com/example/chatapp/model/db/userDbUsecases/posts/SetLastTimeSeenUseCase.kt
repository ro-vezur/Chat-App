package com.example.chatapp.model.db.userDbUsecases.posts

import com.example.chatapp.Dtos.user.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.example.chatapp.model.db.userDbUsecases.gets.GetCurrentUserIdUseCase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SetLastTimeSeenUseCase @Inject constructor(
    private val db: FirebaseFirestore,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
) {
    val usersDb = db.collection(USERS_DB_COLLECTION)

    suspend operator fun invoke(time: Long?) {
        db.runTransaction { transaction ->
            val userDocumentRef = usersDb.document(getCurrentUserIdUseCase())
            val user = transaction[userDocumentRef].toObject(User::class.java)

            when {
                user == null -> return@runTransaction
                else -> {
                    transaction.update(userDocumentRef,"seenLastTimeTimeStamp", time)
                }
            }
        }.await()
    }

}