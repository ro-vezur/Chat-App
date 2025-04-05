package com.example.chatapp.model.db.chatDb.usecases.posts.usersTyping

import com.example.chatapp.CHATS_COLLECTION
import com.example.chatapp.USERS_TYPING_IN_CHAT
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class AddUserTypingUseCase @Inject constructor(
    private val db: DatabaseReference
) {
    operator fun invoke(chatId: String,userId: String) {
        val typingUserRef = db.child(CHATS_COLLECTION).child(chatId).child(USERS_TYPING_IN_CHAT).child(userId)
        typingUserRef.get().addOnSuccessListener { dataSnapshot ->
            if(!dataSnapshot.exists()) {
                typingUserRef.setValue(userId)
            }
        }

    }
}