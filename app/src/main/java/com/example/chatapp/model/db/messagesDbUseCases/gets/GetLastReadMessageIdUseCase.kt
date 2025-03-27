package com.example.chatapp.model.db.messagesDbUseCases.gets

import com.example.chatapp.CHATS_DB_COLLECTION
import com.example.chatapp.Dtos.chat.Chat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetLastReadMessageIdUseCase @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend operator fun invoke(chatId: String, userId: String): String? {
        return try {
            val chatRef = firestore.collection(CHATS_DB_COLLECTION).document(chatId).get().await()
            val chat = chatRef.toObject<Chat>()

            when {
                chat == null -> null
                !chat.lastReads.contains(userId) -> null
                else -> chat.lastReads[userId]
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}