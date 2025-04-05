package com.example.chatapp.model.db.chatDb.usecases.posts

import com.example.chatapp.CHATS_COLLECTION
import com.example.chatapp.Dtos.chat.Chat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AddChatUseCase @Inject constructor(
    private val fireStore: FirebaseFirestore,
) {
    private val chatsCollection = fireStore.collection(CHATS_COLLECTION)
    suspend operator fun invoke(chat: Chat,onSuccess: () -> Unit) {
        try {
            chatsCollection.document(chat.id).set(chat).await()
            onSuccess()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}