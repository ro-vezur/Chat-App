package com.example.chatapp.others.states.DirectionalLazyListState

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue

class DirectionalLazyListState(
    private val lazyListState: LazyListState,
) {
    private var lastOffset = lazyListState.firstVisibleItemScrollOffset
    private var visibleItem = lazyListState.firstVisibleItemIndex

    val scrollDirection by derivedStateOf {
        if(lazyListState.isScrollInProgress.not()) {
            ScrollDirection.NONE
        } else {
            val firstVisibleItemScrollOffset = lazyListState.firstVisibleItemScrollOffset
            val firstVisibleItemIndex = lazyListState.firstVisibleItemIndex

            if(firstVisibleItemIndex == visibleItem) {
                val direction = if(firstVisibleItemScrollOffset > lastOffset) {
                    ScrollDirection.DOWN
                } else {
                    ScrollDirection.UP
                }
                lastOffset = firstVisibleItemScrollOffset

                direction
            } else {
                val direction = if(firstVisibleItemIndex > visibleItem) {
                    ScrollDirection.DOWN
                } else {
                    ScrollDirection.UP
                }

                lastOffset = firstVisibleItemScrollOffset
                visibleItem = firstVisibleItemIndex

                direction
            }
        }
    }
}