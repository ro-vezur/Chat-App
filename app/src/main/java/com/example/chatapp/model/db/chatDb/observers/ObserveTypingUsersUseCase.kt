package com.example.chatapp.model.db.chatDb.observers

import com.example.chatapp.CHATS_COLLECTION
import com.example.chatapp.USERS_TYPING_IN_CHAT
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ObserveTypingUsersUseCase @Inject constructor(
    private val db: DatabaseReference
) {
    operator fun invoke(chatId: String) = callbackFlow<List<String>> {
        val chatRef = db.child(CHATS_COLLECTION).child(chatId).child(USERS_TYPING_IN_CHAT)

        val typingUsersListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val typingUsers = snapshot.children.mapNotNull { it.getValue(String::class.java) }
                trySend(typingUsers)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        chatRef.addValueEventListener(typingUsersListener)

        awaitClose {
            chatRef.removeEventListener(typingUsersListener)
        }

    }
}