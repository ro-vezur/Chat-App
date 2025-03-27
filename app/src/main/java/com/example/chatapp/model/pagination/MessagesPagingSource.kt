package com.example.chatapp.model.pagination

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.chatapp.Dtos.chat.Message
import com.google.firebase.database.Query
import kotlinx.coroutines.tasks.await


class MessagesPagingSource (
    private val query: Query,
    private val pageSize: Int,
    private val lastReadTimeStamp: Long?,
    private val firstTimestampKey: MutableState<Long?>,
    private val lastTimestampKey: MutableState<Long?>,
) : PagingSource<Int, Message>() {

    override fun getRefreshKey(state: PagingState<Int, Message>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Message> {
        return try {

            val page = params.key ?: 1

      //      Log.d("page",page.toString())

            val queryToExecute = when(params) {
                is LoadParams.Refresh -> {
                    when {
                        lastReadTimeStamp == null -> {
                            Log.d("last msgs fetch","!!!")
                            query.limitToLast(pageSize)
                        }
                        firstTimestampKey.value == null && lastTimestampKey.value == null -> {
                            Log.d("first last read msg fetch",lastReadTimeStamp.toString())
                            query
                                .endAt(lastReadTimeStamp.toDouble())
                                .limitToLast(pageSize)
                        }
                        else -> {
                            Log.d("else",lastReadTimeStamp.toString())
                            query.limitToLast(pageSize)
                        }
                    }
                }
                is LoadParams.Prepend -> {
                    Log.d("firstTimestampKey",firstTimestampKey.toString())
                    query
                        .startAfter(firstTimestampKey.value?.toDouble() ?: Double.MAX_VALUE)
                        .limitToLast(pageSize)
                }
                is LoadParams.Append -> {
                    Log.d("lastTimestampKey",lastTimestampKey.toString())
                    query
                        .endBefore(lastTimestampKey.value?.toDouble() ?: 0.0)
                        .limitToLast(pageSize)
                }
            }

            val seenSnapshot = queryToExecute.get().await()
            val messages = seenSnapshot.children.mapNotNull { it.getValue(Message::class.java) }
                .sortedByDescending { it.sentTimeStamp }

        //    Log.d("messages is empty?",messages.isEmpty().toString())

            if(messages.isNotEmpty()) {
                /*
                when {
                    firstTimestampKey.value == null && lastTimestampKey.value == null -> {
                        firstTimestampKey.value = messages.last().sentTimeStamp
                        lastTimestampKey.value = messages.first().sentTimeStamp
                        Log.d("last timestamp key",messages.last().content)
                        Log.d("first timestamp key", messages.first().content)
                    }
                    params is LoadParams.Append -> lastTimestampKey.value = messages.first().sentTimeStamp
                    params is LoadParams.Prepend -> firstTimestampKey.value = messages.last().sentTimeStamp
                }
                 */
                firstTimestampKey.value = messages.first().sentTimeStamp
                lastTimestampKey.value = messages.last().sentTimeStamp

                Log.d("messages content",messages.map { it.content }.toString())
            }

            LoadResult.Page(
                data = messages,
                prevKey = if(messages.isEmpty()) null else page - 1,
                nextKey = if(messages.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}