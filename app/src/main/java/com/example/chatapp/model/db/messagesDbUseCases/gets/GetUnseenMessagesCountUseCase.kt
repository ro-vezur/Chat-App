package com.example.chatapp.model.db.messagesDbUseCases.gets

import com.example.chatapp.CHATS_COLLECTION
import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.MESSAGES_DB
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.getValue
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetUnseenMessagesCountUseCase @Inject constructor(
    private val db: DatabaseReference
) {
    suspend operator fun invoke(chatId: String,lastReadMessage: Message,mainUserId: String,): Int {
        return try {
            val chatRef = db.child(CHATS_COLLECTION).child(chatId).child(MESSAGES_DB).orderByChild("sentTimeStamp")
                .startAfter(lastReadMessage.sentTimeStamp?.toDouble() ?: 0.0)

            val dataSnapshot = chatRef.get().await()
            dataSnapshot.children.count { it.child("userId").getValue<String>() != mainUserId }
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }
}