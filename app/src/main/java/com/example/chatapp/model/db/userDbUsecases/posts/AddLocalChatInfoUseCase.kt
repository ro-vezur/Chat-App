package com.example.chatapp.model.db.userDbUsecases.posts

import com.example.chatapp.Dtos.chat.LocalChatInfo
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AddLocalChatInfoUseCase @Inject constructor(
    private val db: FirebaseFirestore,
) {
    val usersDb = db.collection(USERS_DB_COLLECTION)

    suspend operator fun invoke(userId: String,localChatInfo: LocalChatInfo) {
        try {
            db.runTransaction { transaction ->
                val userDocument = usersDb.document(userId)
                val user = transaction[userDocument].toObject<User>()

                when {
                    user == null -> return@runTransaction
                    user.localChats.map { it.id }.contains(localChatInfo.id) -> return@runTransaction
                    else -> {
                        val localChats = user.localChats
                        localChats.add(localChatInfo)
                        transaction.update(userDocument,"localChats",localChats)
                    }
                }
            }.await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}