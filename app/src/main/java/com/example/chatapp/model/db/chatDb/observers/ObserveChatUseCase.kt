package com.example.chatapp.model.db.chatDb.observers

import com.example.chatapp.CHATS_DB
import com.example.chatapp.Dtos.chat.Chat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ObserveChatUseCase @Inject constructor(
    db: FirebaseFirestore,
) {
    private val chatDb = db.collection(CHATS_DB)

    operator fun invoke(chatId: String) = callbackFlow {
        val listener = chatDb.document(chatId).addSnapshotListener { snapshot, error ->
            if(error != null) {
                close(error)
                return@addSnapshotListener
            }

            val chat = snapshot?.toObject(Chat::class.java)

            if(chat != null) {
                trySend(chat)
            }
        }

        awaitClose { listener.remove() }
    }

}