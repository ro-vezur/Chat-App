package com.example.chatapp.model.db.messagesDbUseCases.posts

import android.util.Log
import com.example.chatapp.CHATS_DB
import com.example.chatapp.Dtos.chat.Chat
import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.MESSAGES_DB
import com.example.chatapp.helpers.time.getCurrentTimeInMillis
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class EditMessageUseCase @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val db: DatabaseReference,
) {
    suspend operator fun invoke(newMessage: Message) {
        try {
            val messageRef = db.child(CHATS_DB).child(newMessage.chatId).child(MESSAGES_DB).child(newMessage.id)
            Log.d("msg ref",messageRef.toString())
            Log.d("new msg object",newMessage.toString())
            messageRef.setValue(newMessage).await()
            firestore.runTransaction { transaction ->
                val chatRef = firestore.collection(CHATS_DB).document(newMessage.chatId)
                val chat = transaction[chatRef].toObject<Chat>()

                chat?.let {
                    if(chat.messages.lastOrNull() == newMessage.id) {
                        transaction.update(chatRef,"lastUpdateTimestamp", getCurrentTimeInMillis())
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}