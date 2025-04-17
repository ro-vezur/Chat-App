package com.example.chatapp.model.db.messagesDbUseCases.posts

import com.example.chatapp.CHATS_DB
import com.example.chatapp.Dtos.chat.Chat
import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.MESSAGES_DB
import com.example.chatapp.helpers.time.getCurrentTimeInMillis
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AddMessageUseCase @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val db: DatabaseReference,
) {
    private val chatsDb = fireStore.collection(CHATS_DB)

    suspend operator fun invoke(message: Message) {
        try {
            fireStore.runTransaction { transaction ->
                val chatDocumentRef = chatsDb.document(message.chatId)
                val chat = transaction[chatDocumentRef].toObject(Chat::class.java)

                chat?.let {
                    val messagesInFirestore = chat.messages
                    messagesInFirestore.add(message.id)

                    db.child(CHATS_DB).child(message.chatId).child(MESSAGES_DB).child(message.id).setValue(message)
                    transaction.set(
                        chatDocumentRef,
                        chat.copy(
                            messages = messagesInFirestore,
                            lastUpdateTimestamp = getCurrentTimeInMillis()
                        )
                    )
                }
            }.await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}