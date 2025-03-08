package com.example.chatapp.model.db.messagesDbUseCases.posts

import com.example.chatapp.CHATS_DB_COLLECTION
import com.example.chatapp.Dtos.chat.Chat
import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.MESSAGES_DB_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AddMessageUseCase @Inject constructor(
    private val db: FirebaseFirestore
) {
    private val chatsDb = db.collection(CHATS_DB_COLLECTION)
    private val messagesDb = db.collection(MESSAGES_DB_COLLECTION)

    suspend operator fun invoke(message: Message) {
        try {
            db.runTransaction { transaction ->
                val chatDocumentRef = chatsDb.document(message.chatId)
                val chat = transaction[chatDocumentRef].toObject(Chat::class.java)

                chat?.let {
                    val messagesIDs = chat.messages
                    messagesIDs.add(0,message.id)
                    messagesDb.document(message.id).set(message)

                    transaction.update(chatDocumentRef,"messages",messagesIDs)
                }
            }.await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}