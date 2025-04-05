package com.example.chatapp.model.db.messagesDbUseCases.posts

import android.util.Log
import com.example.chatapp.CHATS_COLLECTION
import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.MESSAGES_DB
import com.example.chatapp.helpers.time.getCurrentTimeInMillis
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import javax.inject.Inject

class SetMessagesReadStatusUseCase @Inject constructor(
    private val db: DatabaseReference,
) {
    operator fun invoke(messagesReadList: List<Message>, chatId: String,userId: String,onSuccess: () -> Unit) {
        try {
            messagesReadList.forEach { message ->
                val messageRef = db.child(CHATS_COLLECTION).child(chatId).child(MESSAGES_DB).child(message.id)
                messageRef.runTransaction(
                    object : Transaction.Handler {
                        override fun doTransaction(currentData: MutableData): Transaction.Result {
                            val seenBy = message.seenBy
                            if(!seenBy.contains(userId)) {
                                seenBy[userId] = getCurrentTimeInMillis()
                            }
                            currentData.value = message

                            return Transaction.success(currentData)
                        }

                        override fun onComplete(
                            error: DatabaseError?,
                            committed: Boolean,
                            currentData: DataSnapshot?,
                        ) {}
                    }
                )
            }

            onSuccess()
        } catch (e: Exception) {
            Log.e("ERROR TO UPDATE LAST READS",e.message.toString())
            e.printStackTrace()
        }

    }
}