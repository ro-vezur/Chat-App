package com.example.chatapp.model.db.chatDb

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.chatapp.CHATS_DB
import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.MESSAGES_DB
import com.example.chatapp.MESSAGES_PER_PAGE
import com.example.chatapp.model.db.messagesDbUseCases.gets.GetChatMessageUseCase
import com.example.chatapp.model.db.messagesDbUseCases.gets.GetLastReadMessageIdUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetCurrentUserIdUseCase
import com.example.chatapp.model.pagination.MessagesPagingSource
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class ChatPagingRepository @Inject constructor(
    private val db: DatabaseReference,
    private val getLastReadMessageIdUseCase: GetLastReadMessageIdUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getChatMessageUseCase: GetChatMessageUseCase,
) {
    private var firstTimestampKey: MutableState<Long?> = mutableStateOf(null)
    private var anchorPosition: MutableState<Long?> = mutableStateOf(null)

    private var pagingSource: MessagesPagingSource? = null

    suspend fun getPaginatedMessages(chatId: String): Flow<PagingData<Message>> {

        return try {
            val lastReadMessageId = getLastReadMessageIdUseCase(chatId,getCurrentUserIdUseCase())
            val lastReadMessage = if(lastReadMessageId != null)  getChatMessageUseCase(chatId,lastReadMessageId) else null

            Pager(
                config = PagingConfig(
                    pageSize = MESSAGES_PER_PAGE,
                    enablePlaceholders = true,
                    prefetchDistance = 5,
                ),
                pagingSourceFactory = { createPagingSource(chatId,lastReadMessage) },
            ).flow
        } catch (e: Exception) {
            e.printStackTrace()
            emptyFlow()
        }
    }

    private fun createPagingSource(chatId: String,lastReadMessage: Message?): MessagesPagingSource {
        val query =  db.child(CHATS_DB).child(chatId).child(MESSAGES_DB).orderByChild("sentTimeStamp")

        pagingSource = MessagesPagingSource(
            query = query,
            pageSize = MESSAGES_PER_PAGE,
            lastReadTimeStamp = lastReadMessage?.sentTimeStamp,
            firstTimestampKey = firstTimestampKey,
            anchorPosition = anchorPosition,
        )

        return pagingSource!!
    }

    private fun refreshMessages() {
        pagingSource?.invalidate()
    }

    fun messagesListener(chatId: String) {
        val chatRef = db.child(CHATS_DB).child(chatId).child(MESSAGES_DB)

        val messagesListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if(snapshot.exists() && snapshot.hasChildren()) {
                    refreshMessages()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                if(snapshot.exists() && snapshot.hasChildren()) {
                    refreshMessages()
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                if(snapshot.exists() && snapshot.hasChildren()) {
                    refreshMessages()
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        chatRef.removeEventListener(messagesListener)
        chatRef.addChildEventListener(messagesListener )
    }
}