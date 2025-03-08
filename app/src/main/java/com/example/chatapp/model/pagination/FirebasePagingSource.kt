package com.example.chatapp.model.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await


class FirebasePagingSource <T: Any> (
    private val query: Query,
    private val documentParser: (DocumentSnapshot) -> T,
) : PagingSource<DocumentSnapshot, T>() {
    override fun getRefreshKey(state: PagingState<DocumentSnapshot, T>): DocumentSnapshot? = null

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, T> {
        return try {
            val currentPage = params.key?.let {
                query.startAfter(it).get().await()
            } ?: query.get().await()

            val lastVisibleDoc = currentPage.documents.lastOrNull()
            val items = currentPage.mapNotNull { documentParser(it) }

            LoadResult.Page(
                data = items,
                prevKey = null,
                nextKey = lastVisibleDoc
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}