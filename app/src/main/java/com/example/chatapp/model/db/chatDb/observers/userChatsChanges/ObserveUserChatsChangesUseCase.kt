package com.example.chatapp.model.db.chatDb.observers.userChatsChanges

import android.util.Log
import com.example.chatapp.CHATS_COLLECTION
import com.example.chatapp.Dtos.chat.Chat
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ObserveUserChatsChangesUseCase @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    operator fun invoke(
        userId: String,
    ) = callbackFlow<ChatChange> {
        val chatsListener = firestore.collection(CHATS_COLLECTION)
            .whereArrayContains("users",userId)
            .addSnapshotListener { snapshot, error ->
                if(error != null) {
                    return@addSnapshotListener
                }
                snapshot?.let {
                  //  Log.d("documentChanges",snapshot.documentChanges.size.toString())
                    snapshot.documentChanges.forEach { documentChange ->
                        val chat = documentChange.document.toObject<Chat>()

                        when(documentChange.type) {
                            DocumentChange.Type.ADDED -> {
                                Log.d("chat on ADD change",chat.toString())
                            }
                            DocumentChange.Type.MODIFIED -> {
                                Log.d("chat on MODIFIED change",chat.usersTyping.toString())

                                trySend(ChatChange.Updated(chat))
                            }
                            DocumentChange.Type.REMOVED -> {
                                Log.d("chat on REMOVED change",chat.toString())
                            }
                        }
                    }
                }
            }

        awaitClose {
            chatsListener.remove()
        }
    }
}