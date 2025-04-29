package com.example.chatapp.model.db.chatDb.usecases.gets

import com.example.chatapp.CHATS_DB
import com.example.chatapp.Dtos.chat.Chat
import com.example.chatapp.Dtos.chat.enums.ChatType
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetOneToOneChatUseCase @Inject constructor(
    db: FirebaseFirestore
) {
    private val chatsDb = db.collection(CHATS_DB)

    suspend operator fun invoke(usersIds: List<String>): Chat? {
        return try {
            val querySnapshot = chatsDb.whereEqualTo("chatType", ChatType.USER.name).get().await()

            val matchingDocs = querySnapshot.documents.filter { document ->
                val usersFilterList = document.get("users") as? List<String> ?: emptyList()
                usersFilterList.sorted() == usersIds.sorted()
            }

            when {
                matchingDocs.isEmpty() -> null
                else -> matchingDocs.first().toObject(Chat::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}