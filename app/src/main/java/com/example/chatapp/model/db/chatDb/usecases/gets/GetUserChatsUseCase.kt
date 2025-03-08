package com.example.chatapp.model.db.chatDb.usecases.gets

import com.example.chatapp.CHATS_DB_COLLECTION
import com.example.chatapp.Dtos.chat.ChatUI
import com.example.chatapp.Dtos.chat.LocalChatInfo
import com.example.chatapp.others.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class GetUserChatsUseCase @Inject constructor(
    db: FirebaseFirestore,
) {
    private val chatsDb = db.collection(CHATS_DB_COLLECTION)

    operator fun invoke(userId: String, contacts: List<LocalChatInfo> ): Flow<Resource<List<ChatUI>>> = callbackFlow {
        val listener = chatsDb.whereArrayContains("users",userId)
            .addSnapshotListener { snapshot, error ->
                if(error != null) {
                    trySend(Resource.Error(message = error.message.toString()))
                    return@addSnapshotListener
                }

                val chats = snapshot?.documents?.mapNotNull { chatDocument ->
                    val contact = contacts.find { contact -> contact.id == chatDocument.id }
                    val isPinned = contact != null && contact.isPinned

                    val chat = chatDocument.toObject(ChatUI::class.java)

                    chat?.copy(
                        isPinned = isPinned,
                        name = contact?.name ?: chat.name,
                        imageUrl = contact?.imageUrl ?: chat.imageUrl,
                        lastMessage = "",
                    )
                } ?: emptyList()
                trySend(Resource.Success(chats))
            }

        awaitClose { listener.remove() }
    }
}