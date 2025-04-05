package com.example.chatapp.model.db.messagesDbUseCases.gets

import android.util.Log
import com.example.chatapp.CHATS_COLLECTION
import com.example.chatapp.Dtos.chat.Chat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class GetAllUserChatsUnseenMessagesCountUseCase @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    operator fun invoke(userId: String) = callbackFlow<Int> {
        val userChats = firestore.collection(CHATS_COLLECTION)
            .whereArrayContains("users",userId)
            .addSnapshotListener { snapshot, error ->
                if(error != null) {
                    trySend(0)
                    return@addSnapshotListener
                }

                snapshot?.let {
                    snapshot.documentChanges.forEach { changes ->
                        Log.d("change",changes.type.name)
                    }

                    val chatsObjects = snapshot.toObjects(Chat::class.java)
                    val unseenMessagesCount = chatsObjects.sumOf { chat ->
                        val lastUserReadMessageId = chat.lastReads[userId]
                        Log.d("chat msgs",chat.messages.size.toString())
                        chat.messages.dropWhile { msgId -> msgId != lastUserReadMessageId }.size
                    }

                    Log.d("unseen msgs count from all chats",unseenMessagesCount.toString())

                    trySend(unseenMessagesCount)
                }

        }

        awaitClose {
            userChats.remove()
        }
    }
}