package com.example.chatapp.model.db.chatDb

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class ChatPagingRepository @Inject constructor(
    private val db: DatabaseReference,
    private val getLastReadMessageIdUseCase: GetLastReadMessageIdUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getChatMessageUseCase: GetChatMessageUseCase,
) {
    private val refreshTrigger = MutableStateFlow(0)
    private var firstTimestampKey: MutableState<Long?> = mutableStateOf(null)
    private var lastTimestampKey: MutableState<Long?> = mutableStateOf(null)
    private var pagingSource: MessagesPagingSource? = null

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    suspend fun getPaginatedMessages(chatId: String): Flow<PagingData<Message>> {

        return try {
            refreshTrigger
                .flatMapLatest {
                val query =  db.child(MESSAGES_DB).child(chatId).orderByChild("sentTimeStamp")

                val lastReadMessageId = getLastReadMessageIdUseCase(chatId,getCurrentUserIdUseCase())
                val lastReadMessage = if(lastReadMessageId != null) getChatMessageUseCase(chatId,lastReadMessageId) else null


              //  Log.d("last read msg",lastReadMessage.toString())

               // Log.d("refresh trigger counter",it.toString())

                Pager(
                    config = PagingConfig(
                        pageSize = MESSAGES_PER_PAGE,
                        prefetchDistance = 5
                    ),
                    pagingSourceFactory = {
                        pagingSource = MessagesPagingSource(
                            query = query,
                            pageSize = MESSAGES_PER_PAGE,
                            lastReadTimeStamp = lastReadMessage?.sentTimeStamp,
                            firstTimestampKey = firstTimestampKey,
                            lastTimestampKey = lastTimestampKey,
                        )

                        pagingSource!!
                    },
                ).flow
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyFlow()
        }
    }

    fun messagesListener(chatId: String) {
        val chatRef = db.child(MESSAGES_DB).child(chatId)

        val messagesListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if(snapshot.exists() && snapshot.hasChildren()) {
                    invalidatePagingSource()
                    refreshTrigger.value += 1
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                if(snapshot.exists() && snapshot.hasChildren()) {
                    invalidatePagingSource()
                    refreshTrigger.value += 1
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                if(snapshot.exists() && snapshot.hasChildren()) {
                    invalidatePagingSource()
                    refreshTrigger.value += 1
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                if(snapshot.exists() && snapshot.hasChildren()) {
                    invalidatePagingSource()
                    refreshTrigger.value += 1
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        chatRef.removeEventListener(messagesListener)
        chatRef.addChildEventListener(messagesListener )
    }

    private fun invalidatePagingSource() {
        if(pagingSource?.invalid == false) {
          //  pagingSource?.invalidate()
        }
    }
}