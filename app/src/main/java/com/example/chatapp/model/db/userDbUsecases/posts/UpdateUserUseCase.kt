package com.example.chatapp.model.db.userDbUsecases.posts

import com.example.chatapp.CHATS_DB
import com.example.chatapp.Dtos.chat.chatType.ChatType
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.example.chatapp.helpers.time.getCurrentTimeInMillis
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend operator fun invoke(user: User) {
        try {
            val batch = firestore.batch()

            firestore.runTransaction { transaction ->
                val userRef = firestore.collection(USERS_DB_COLLECTION).document(user.id)
                val userSnapshot = transaction[userRef]

                if(userSnapshot.exists()) {
                    transaction.set(userRef,user)
                }
            }.await()

            val chatsQuery = firestore.collection(CHATS_DB)
                .whereArrayContains("users",user.id)
                .whereEqualTo("type",ChatType.USER)

            chatsQuery.get().await().documents.forEach { documentSnapshot ->
                val chatRef = firestore.collection(CHATS_DB).document(documentSnapshot.id)
                batch.update(chatRef,"lastUpdateTimestamp", getCurrentTimeInMillis())
            }

            batch.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}