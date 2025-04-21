package com.example.chatapp.model.pagination

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.chatapp.Dtos.chat.Chat
import com.example.chatapp.Dtos.chat.ChatUI
import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.Dtos.chat.chatType.ChatType
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.USER_CHATS_PER_PAGE
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class UserChatsPagingSource (
    private val query: Query,
    private val userId: String,
    private val getChatMessage: suspend (String,String) -> Message,
    private val getUser: suspend (String) -> User?,
    private val chatIsPinned: (String) -> Boolean,
): PagingSource<Long, ChatUI>() {
    override fun getRefreshKey(state: PagingState<Long, ChatUI>): Long? {
        return state.anchorPosition?.let { state.closestItemToPosition(it)?.lastUpdateTimestamp }
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, ChatUI> {
        return try {
            val key = params.key

            val queryToExecute = when (params) {
                is LoadParams.Append -> query.startAfter(key)
                is LoadParams.Prepend -> query.endBefore(key)
                is LoadParams.Refresh -> query.limitToLast(USER_CHATS_PER_PAGE)
            }

            val chatsObjects = queryToExecute.get().await().toObjects(Chat::class.java)
            val chatsUIObjects = chatsObjects.mapNotNull { chat ->
                val lastReadMessage = chat.lastReads[userId]
                val oppositeUser = if (chat.chatType == ChatType.USER) getUser(chat.getOppositeUserId(userId) ?: "") else null
                val typingUsersText = when {
                    chat.usersTyping.isEmpty() -> null
                    chat.usersTyping.size == 1 && chat.usersTyping.contains(userId) -> null
                    chat.usersTyping.size == 1 -> "${getUser(chat.usersTyping.first())?.name} is Typing"
                    chat.usersTyping.size > 1 -> "${chat.usersTyping.filter { it != userId }.size} are Typing"
                    else -> null
                }

                if(oppositeUser == null) {
                    ChatUI(
                        id = chat.id,
                        chatType = chat.chatType,
                        lastMessage = getChatMessage(chat.id,chat.messages.lastOrNull() ?: ""),
                        isPinned = chatIsPinned(chat.id),
                        name = chat.name,
                        imageUrl = chat.imageUrl,
                        user = null,
                        unseenMessagesCount = chat.messages.dropWhile { messageId -> messageId != lastReadMessage}.drop(1).size,
                        typingUsersText = typingUsersText,
                        lastUpdateTimestamp = chat.lastUpdateTimestamp
                    )
                } else {
                    ChatUI(
                        id = chat.id,
                        chatType = chat.chatType,
                        lastMessage = getChatMessage(chat.id, chat.messages.lastOrNull() ?: ""),
                        isPinned = chatIsPinned(chat.id),
                        name = oppositeUser.getOppositeUserName(userId),
                        imageUrl = oppositeUser.getOppositeUserImage(userId),
                        user = oppositeUser,
                        unseenMessagesCount = chat.messages.dropWhile { messageId -> messageId != lastReadMessage }
                            .drop(1).size,
                        typingUsersText = typingUsersText,
                        lastUpdateTimestamp = chat.lastUpdateTimestamp
                    )
                }
            }


            LoadResult.Page(
                data = chatsUIObjects,
                prevKey = if (chatsUIObjects.isEmpty()) null else chatsUIObjects.last().lastUpdateTimestamp,
                nextKey = if (chatsUIObjects.isEmpty()) null else chatsUIObjects.first().lastUpdateTimestamp
            )
        } catch (e: Exception) {
            Log.e("paging e",e.message.toString())
            LoadResult.Error(e)
        }
    }

}