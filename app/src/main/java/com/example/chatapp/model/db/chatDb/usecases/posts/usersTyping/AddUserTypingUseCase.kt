package com.example.chatapp.model.db.chatDb.usecases.posts.usersTyping

import com.example.chatapp.CHATS_COLLECTION
import com.example.chatapp.Dtos.chat.Chat
import com.example.chatapp.USERS_TYPING_IN_CHAT
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AddUserTypingUseCase @Inject constructor(
    private val db: DatabaseReference,
    private val firestore: FirebaseFirestore,
) {
    suspend operator fun invoke(chatId: String,userId: String) {
        if(chatId.isNotBlank()) {
            val typingUserRef =
                db.child(CHATS_COLLECTION).child(chatId).child(USERS_TYPING_IN_CHAT).child(userId)
            val dataSnapshot = typingUserRef.get().await()

            if (!dataSnapshot.exists()) {
                typingUserRef.setValue(userId)
            }

            val chatRef = firestore.collection(CHATS_COLLECTION).document(chatId)
            firestore.runTransaction { transaction ->
                val chatObject = transaction[chatRef].toObject<Chat>()

                chatObject?.let {
                    val usersTyping = chatObject.usersTyping
                    if (!usersTyping.contains(userId)) {
                        usersTyping.add(userId)
                        transaction.update(chatRef, "usersTyping", usersTyping)
                    }
                }
            }
        }
    }
}