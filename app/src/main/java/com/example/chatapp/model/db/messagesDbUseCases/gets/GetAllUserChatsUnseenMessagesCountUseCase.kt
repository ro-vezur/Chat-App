package com.example.chatapp.model.db.messagesDbUseCases.gets

import com.example.chatapp.CHATS_COLLECTION
import com.example.chatapp.Dtos.chat.Chat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class GetAllUserChatsUnseenMessagesCountUseCase @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val chatsCollection = firestore.collection(CHATS_COLLECTION)

    operator fun invoke(userId: String) = callbackFlow<Int> {
        val userChats = chatsCollection
            .whereArrayContains("users",userId)
            .addSnapshotListener { snapshot, error ->
                snapshot?.let {
                    val chatsObjects = snapshot.toObjects(Chat::class.java)

                    val unseenMessagesCount = chatsObjects.sumOf { chat ->
                        val lastUserReadMessageId = chat.lastReads[userId]
                        chat.messages.dropWhile { msgId -> msgId != lastUserReadMessageId }.drop(1).size
                    }

                    trySend(unseenMessagesCount)
                }
            }
        awaitClose {
            userChats.remove()
        }
    }.catch { e ->
        e.printStackTrace()
        emit(0)
    }
}