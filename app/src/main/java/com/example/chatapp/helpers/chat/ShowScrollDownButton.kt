package com.example.chatapp.helpers.chat

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue

class ShowScrollDownButton(
    private val lazyListState: LazyListState
) {
    private var positionY = lazyListState.firstVisibleItemScrollOffset
    private var visibleItem = lazyListState.firstVisibleItemIndex

    val showScrollDownButton by derivedStateOf {
        
    }
}