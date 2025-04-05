package com.example.chatapp.model.db.messagesDbUseCases.posts

import com.example.chatapp.CHATS_COLLECTION
import com.example.chatapp.Dtos.chat.Chat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UpdateUserLastSeenMessageIdUseCase @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend operator fun invoke(userId: String, chatId: String, messageId: String) {
        try {
            firestore.runTransaction { transaction ->
                val chatRef = firestore.collection(CHATS_COLLECTION).document(chatId)
                val chat = transaction[chatRef].toObject<Chat>()

                chat?.let {
                    val lastReads = chat.lastReads
                    lastReads[userId] = messageId

                    transaction.update(chatRef, "lastReads", lastReads)
                }
            }.await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}