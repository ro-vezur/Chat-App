package com.example.chatapp.model.db.chatDb

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.MESSAGES_DB_COLLECTION
import com.example.chatapp.MESSAGES_PER_PAGE
import com.example.chatapp.model.pagination.FirebasePagingSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

private var messagesListenerRegistration: ListenerRegistration? = null
private var chatListenerRegistration: ListenerRegistration? = null

class ChatPagingRepository @Inject constructor(
    db: FirebaseFirestore
) {
    private val messagesDb = db.collection(MESSAGES_DB_COLLECTION)
    private val _refreshTrigger = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getPaginatedMessages(chatId: String): Flow<PagingData<Message>> {
        val query = messagesDb
            .whereEqualTo("chatId",chatId)
            .orderBy("sentTimeStamp", Query.Direction.DESCENDING)
            .limit(MESSAGES_PER_PAGE)


        return _refreshTrigger.flatMapLatest {
            Log.d("REFRESH!",_refreshTrigger.value.toString())
            Pager(
                config = PagingConfig(MESSAGES_PER_PAGE.toInt()),
                pagingSourceFactory = {
                    FirebasePagingSource(
                        query = query,
                        documentParser = { documentSnapshot -> documentSnapshot.toObject<Message>()!! }
                    )
                }
            ).flow
        }
    }

    fun messagesListener(chatId: String) {
        messagesListenerRegistration?.remove()

        messagesListenerRegistration = messagesDb
            .whereEqualTo("chatId",chatId)
            .orderBy("sentTimeStamp", Query.Direction.DESCENDING)
            .addSnapshotListener { messagesSnapShots, _ ->
                if(messagesSnapShots != null) {
                    _refreshTrigger.value += 1
                    Log.d("refresh trigger",_refreshTrigger.value.toString())
                }
            }
    }
}