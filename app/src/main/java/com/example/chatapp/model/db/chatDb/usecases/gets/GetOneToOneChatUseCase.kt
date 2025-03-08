package com.example.chatapp.model.db.chatDb.usecases.gets

import android.util.Log
import com.example.chatapp.CHATS_DB_COLLECTION
import com.example.chatapp.Dtos.chat.Chat
import com.example.chatapp.Dtos.chat.ChatType
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class GetOneToOneChatUseCase @Inject constructor(
    db: FirebaseFirestore
) {
    private val chatsDb = db.collection(CHATS_DB_COLLECTION)

    operator fun invoke(usersIds: List<String>,onResult: (Chat?) -> Unit) {
         try {
             chatsDb.whereEqualTo("chatType",ChatType.USER.name).get()
                 .addOnSuccessListener { documents ->
                     val matchingDocs = documents.documents.filter { document ->
                         val usersFilterList = document.get("users") as? List<String> ?: emptyList()
                         usersFilterList.sorted() == usersIds.sorted()
                     }

                     if(matchingDocs.isNotEmpty()) {
                         val chat = matchingDocs.first().toObject(Chat::class.java)
                         onResult(chat)
                     } else {
                         onResult(null)
                     }
                 }
                 .addOnFailureListener { e ->
                     Log.e("error",e.message.toString())
                 }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}