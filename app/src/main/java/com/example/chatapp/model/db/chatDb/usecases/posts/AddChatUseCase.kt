package com.example.chatapp.model.db.chatDb.usecases.posts

import com.example.chatapp.CHATS_DB_COLLECTION
import com.example.chatapp.Dtos.chat.Chat
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AddChatUseCase @Inject constructor(
    private val db: FirebaseFirestore
) {
    private val chatsDb = db.collection(CHATS_DB_COLLECTION)
    operator fun invoke(chat: Chat,onSuccess: () -> Unit) {
        try {
            chatsDb.document(chat.id).set(chat).addOnSuccessListener {
                onSuccess()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}