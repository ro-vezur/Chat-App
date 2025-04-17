package com.example.chatapp.model.db.messagesDbUseCases.posts

import com.example.chatapp.CHATS_DB
import com.example.chatapp.Dtos.chat.Chat
import com.example.chatapp.MESSAGES_DB
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DeleteMessageUseCase @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val db: DatabaseReference
) {
    suspend operator fun invoke(messageId: String, chatId: String) {
        val messageRef = db.child(CHATS_DB).child(chatId).child(MESSAGES_DB).child(messageId)
        val dataSnapshot = messageRef.get().await()
        if(dataSnapshot.exists()) {
            messageRef.removeValue()
        }

        firestore.runTransaction { transaction ->
            val chatRef = firestore.collection(CHATS_DB).document(chatId)
            val chat = transaction[chatRef].toObject<Chat>()

            chat?.let {
                val messages = chat.messages
                if(chat.messages.contains(messageId)) {
                    messages.remove(messageId)
                }

                transaction.update(chatRef,"messages",messages)
            }
        }
    }
}