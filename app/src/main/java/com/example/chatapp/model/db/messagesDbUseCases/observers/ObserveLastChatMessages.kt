package com.example.chatapp.model.db.messagesDbUseCases.observers

import com.example.chatapp.CHATS_DB_COLLECTION
import com.example.chatapp.Dtos.chat.LocalChatInfo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ObserveLastChatMessages @Inject constructor(
    private val db: FirebaseFirestore
){
    private val messagesDb = db.collection(CHATS_DB_COLLECTION)

    operator fun invoke(localChats: List<LocalChatInfo>): Flow<List<String>> = callbackFlow {
        val listener = messagesDb.whereIn("chatId",localChats.map { it.id })
    }
}