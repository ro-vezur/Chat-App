package com.example.chatapp.others.states.DirectionalLazyListState

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberDirectionalLazyListState(
    listState: LazyListState
): DirectionalLazyListState {
    return remember { DirectionalLazyListState(listState) }
}