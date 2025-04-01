package com.example.chatapp.model.db.messagesDbUseCases.gets

import com.example.chatapp.CHATS_DB
import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.MESSAGES_DB
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetUnseenMessagesCountUseCase @Inject constructor(
    private val db: DatabaseReference
) {
    suspend operator fun invoke(chatId: String,lastReadMessage: Message): Int {
        return try {
            val chatRef = db.child(CHATS_DB).child(chatId).child(MESSAGES_DB).orderByChild("sentTimeStamp")
                .startAfter(lastReadMessage.sentTimeStamp?.toDouble() ?: 0.0)
            val dataSnapshot = chatRef.get().await()
            dataSnapshot.childrenCount.toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }
}