package com.example.chatapp.model.db.userDbUsecases.gets

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.chatapp.CHATS_DB
import com.example.chatapp.Dtos.chat.ChatUI
import com.example.chatapp.Dtos.chat.LocalChatInfo
import com.example.chatapp.USER_CHATS_PER_PAGE
import com.example.chatapp.model.db.messagesDbUseCases.gets.GetChatMessageUseCase
import com.example.chatapp.model.pagination.UserChatsPagingSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class GetUserPaginatedChatsUseCase @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val getUserUseCase: GetUserUseCase,
    private val getChatMessageUseCase: GetChatMessageUseCase,
) {
    operator fun invoke(userId: String, contacts: List<LocalChatInfo> ): Flow<PagingData<ChatUI>> {
        if(contacts.isEmpty()) return emptyFlow()

        Log.d("ids",contacts.map { it.id }.toString())

        val query = firestore.collection(CHATS_DB)
            .whereIn("id",contacts.map { it.id })
            .orderBy("lastUpdateTimestamp",Query.Direction.DESCENDING)

        return Pager(
            config = PagingConfig(
                pageSize = USER_CHATS_PER_PAGE.toInt()
            ),
            pagingSourceFactory = {
                UserChatsPagingSource(
                    query = query,
                    userId = userId,
                    getChatMessage = { chatId, messageId ->
                        getChatMessageUseCase(
                            chatId,
                            messageId
                        )
                    },
                    getUser = { getUserUseCase(it) },
                    chatIsPinned = { chatId -> contacts.find { it.id == chatId && it.isPinned } != null }
                )
            }
        ).flow
    }
}