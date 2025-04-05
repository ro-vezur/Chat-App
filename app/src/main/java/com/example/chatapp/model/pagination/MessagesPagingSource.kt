package com.example.chatapp.model.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.chatapp.Dtos.chat.Message
import com.google.firebase.database.Query
import kotlinx.coroutines.tasks.await


class MessagesPagingSource (
    private val query: Query,
    private val pageSize: Int,
    private val lastReadTimeStamp: Long?,
) : PagingSource<Long, Message>() {

    override val jumpingSupported: Boolean
        get() = true

    override fun getRefreshKey(state: PagingState<Long, Message>): Long? = state.anchorPosition?.let { anchor -> state.closestItemToPosition(anchor)?.sentTimeStamp }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Message> {
        return try {

            val key = params.key

            val queryToExecute = when(params) {
                is LoadParams.Refresh -> {
                    when {
                        key != null -> {
                            //Log.d("fetch with key",key.toString())
                            query.limitToLast(pageSize)
                        }
                        lastReadTimeStamp != null -> {
                        //    Log.d("last read fetch","!!!")
                            query.endAt(lastReadTimeStamp.toDouble()).limitToLast(pageSize)
                        }
                        else -> {
                         //   Log.d("else","!!!")
                            query.limitToLast(pageSize)
                        }
                    }
                }
                is LoadParams.Prepend -> {
                 //   Log.d("PREPEND",firstTimestampKey.toString())
                    query
                        .startAfter(key?.toDouble() ?: Double.MAX_VALUE)
                        .limitToLast(pageSize)
                }
                is LoadParams.Append -> {
                //    Log.d("APPEND",lastTimestampKey.toString())
                    query
                        .endBefore(key?.toDouble() ?: 0.0)
                        .limitToLast(pageSize)
                }
            }
            val seenSnapshot = queryToExecute.get().await()
            val messages = seenSnapshot.children.mapNotNull { it.getValue(Message::class.java) }
                .sortedByDescending { it.sentTimeStamp }

            LoadResult.Page(
                data = messages,
                prevKey = if(messages.isEmpty()) null else messages.first().sentTimeStamp,
                nextKey = if(messages.isEmpty()) null else messages.last().sentTimeStamp
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}