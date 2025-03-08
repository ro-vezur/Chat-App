package com.example.chatapp.model.db.messagesDbUseCases.gets

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.MESSAGES_DB_COLLECTION
import com.example.chatapp.MESSAGES_PER_PAGE
import com.example.chatapp.model.pagination.FirebasePagingSource
import com.example.chatapp.others.Resource
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class GetPaginatedChatMessagesUseCase @Inject constructor(
    db: FirebaseFirestore
) {
    private val messagesDb = db.collection(MESSAGES_DB_COLLECTION)
    operator fun invoke(chatId: String): Pager<DocumentSnapshot,Message> {
        val query = messagesDb
            .whereEqualTo("chatId",chatId)
            .orderBy("sentTimeStamp", Query.Direction.DESCENDING)
            .limit(MESSAGES_PER_PAGE)


        return Pager(
            config = PagingConfig(MESSAGES_PER_PAGE.toInt()),
            pagingSourceFactory = {
                FirebasePagingSource(
                    query = query,
                    documentParser = { documentSnapshot -> documentSnapshot.toObject<Message>()!! }
                )
            }
        )
    }
    operator fun invoke(chatId: String, lastMessage: Message? = null): Flow<Resource<List<Message>>> = callbackFlow {
        send(Resource.Loading())


        val query = messagesDb
            .whereEqualTo("chatId",chatId)
            .orderBy("sentTimeStamp", Query.Direction.DESCENDING)
            .limit(MESSAGES_PER_PAGE)

        val paginatedQuery = lastMessage?.let {
            query.startAfter(it.sentTimeStamp)
        } ?: query


        val listener = paginatedQuery.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val updatedMessagesList = mutableListOf<Message>()

            snapshot?.documentChanges?.forEach { change ->
                val message = change.document.toObject<Message>()

                when(change.type) {
                    DocumentChange.Type.ADDED -> updatedMessagesList.add(message)
                    DocumentChange.Type.MODIFIED -> {
                        val messageToModify = updatedMessagesList.find { it.id == message.id }
                        Log.d("messages list",updatedMessagesList.toString())
                        Log.d("message to modify",messageToModify.toString())
                        val index = updatedMessagesList.indexOf(messageToModify)

                        if(index != -1) {
                            updatedMessagesList[index] = message
                        } else return@addSnapshotListener
                    }
                    DocumentChange.Type.REMOVED -> {
                        Log.d("messages list",updatedMessagesList.toString())
                        updatedMessagesList.removeIf { it.id == message.id }
                    }
                }
            }
           // val messages = snapshot?.documents?.mapNotNull { it.toObject(Message::class.java) } ?: emptyList()
            trySend(Resource.Success(data = updatedMessagesList.toList()))
        }

        awaitClose { listener.remove() }
    }.catch { e ->
        Log.e("FETCING PAGIANTED MESSAGES ERROR",e.message.toString())
        emit(Resource.Error(message = e.message.toString()))
    }
}