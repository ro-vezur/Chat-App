package com.example.chatapp.model.db.messagesDbUseCases.gets

import com.example.chatapp.CHATS_DB_COLLECTION
import com.example.chatapp.Dtos.chat.Chat
import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.MESSAGES_DB_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetLastChatMessageUseCase @Inject constructor(
    db: FirebaseFirestore
) {
    private val chatDb = db.collection(CHATS_DB_COLLECTION)
    private val messagesDb = db.collection(MESSAGES_DB_COLLECTION)
    suspend operator fun invoke(chatId: String): String {
        return try {
            val chatDocument = chatDb.document(chatId).get().await()
            val chat = chatDocument.toObject<Chat>()

            when {
                chat == null -> "chat is null"
                chat.messages.isEmpty() -> "chat is empty"
                else -> {
                    val messageDocument = messagesDb.document(chat.messages.first()).get().await()
                    val message = messageDocument.toObject<Message>()
                    message?.content ?: "failed to convert object"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            e.message.toString()
        }
    }
}