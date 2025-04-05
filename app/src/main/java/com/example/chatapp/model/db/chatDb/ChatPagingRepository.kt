package com.example.chatapp.model.db.chatDb

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.chatapp.CHATS_COLLECTION
import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.MESSAGES_DB
import com.example.chatapp.MESSAGES_PER_PAGE
import com.example.chatapp.model.db.messagesDbUseCases.gets.GetChatMessageUseCase
import com.example.chatapp.model.db.messagesDbUseCases.gets.GetLastReadMessageIdUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetCurrentUserIdUseCase
import com.example.chatapp.model.pagination.MessageUpdate
import com.example.chatapp.model.pagination.MessagesPagingSource
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class ChatPagingRepository @Inject constructor(
    private val db: DatabaseReference,
    private val getLastReadMessageIdUseCase: GetLastReadMessageIdUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getChatMessageUseCase: GetChatMessageUseCase,
) {

    private var pagingSource: MessagesPagingSource? = null

    suspend fun getPaginatedMessages(chatId: String): Flow<PagingData<Message>> {

        return try {
            val lastReadMessageId = getLastReadMessageIdUseCase(chatId,getCurrentUserIdUseCase())
            val lastReadMessage = if(lastReadMessageId != null)  getChatMessageUseCase(chatId,lastReadMessageId) else null

            Pager(
                config = PagingConfig(
                    pageSize = MESSAGES_PER_PAGE,
                    prefetchDistance = 15,
                ),
                pagingSourceFactory = { createPagingSource(chatId,lastReadMessage) },
                ).flow
        } catch (e: Exception) {
            e.printStackTrace()
            emptyFlow()
        }
    }

    private fun createPagingSource(chatId: String,lastReadMessage: Message?): MessagesPagingSource {
        val query =  db.child(CHATS_COLLECTION).child(chatId).child(MESSAGES_DB).orderByChild("sentTimeStamp")

        pagingSource = MessagesPagingSource(
            query = query,
            pageSize = MESSAGES_PER_PAGE,
            lastReadTimeStamp = lastReadMessage?.sentTimeStamp,
        )

        return pagingSource!!
    }

    fun messagesListener(chatId: String) = callbackFlow {
        val chatRef = db.child(CHATS_COLLECTION).child(chatId).child(MESSAGES_DB)

        var isInitialLoad = true
        val processedMessagesIds = mutableListOf<String>()

        val initialLoadListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { child ->
                    child.key?.let { processedMessagesIds.add(it) }
                }
                isInitialLoad = false
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        chatRef.addListenerForSingleValueEvent(initialLoadListener)

        val messagesListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if(!isInitialLoad) {
                    snapshot.getValue(Message::class.java)?.let { message ->
                        trySend(MessageUpdate.Added(message))
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(Message::class.java)?.let { message ->
                    trySend(MessageUpdate.Updated(message))
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                snapshot.getValue(Message::class.java)?.let { message ->
                    trySend(MessageUpdate.Removed(message))
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        chatRef.addChildEventListener(messagesListener )

        awaitClose {
            chatRef.removeEventListener(messagesListener)
        }
    }
}