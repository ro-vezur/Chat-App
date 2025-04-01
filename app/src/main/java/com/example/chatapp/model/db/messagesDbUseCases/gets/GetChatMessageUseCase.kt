package com.example.chatapp.model.db.messagesDbUseCases.gets

import com.example.chatapp.CHATS_DB
import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.MESSAGES_DB
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetChatMessageUseCase @Inject constructor(
    private val db: DatabaseReference
) {
    suspend operator fun invoke(chatId: String,id: String): Message {
        return try {
            val dataSnapshot = db.child(CHATS_DB).child(chatId).child(MESSAGES_DB).get().await()
            val message = dataSnapshot.child(id).getValue(Message::class.java)

            return message ?: Message()
        } catch (e: Exception) {
            e.printStackTrace()
            Message()
        }
    }

}