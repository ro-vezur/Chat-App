package com.example.chatapp.differentScreensSupport

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val Int.sdp: Dp @Composable get() = convertToSDP(value = this)

@Composable
private fun convertToSDP(value: Int): Dp {
    val rememberWindowsSize = rememberWindowsSizeClass()

    return when(rememberWindowsSize.screenWidthInfo) {
        WindowInfo.WindowType.Compact -> {value.dp}
        WindowInfo.WindowType.Medium -> {value.dp * 1.2f}
        WindowInfo.WindowType.Expanded -> {value.dp * 1.4f}
    }
}