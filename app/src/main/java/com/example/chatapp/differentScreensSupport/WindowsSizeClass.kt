package com.example.chatapp.differentScreensSupport

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun rememberWindowsSizeClass(): WindowInfo {
    val configuration = LocalConfiguration.current

    return WindowInfo(
        screenHeightInfo = when {
            configuration.screenHeightDp < 600 -> WindowInfo.WindowType.Compact
            configuration.screenHeightDp < 1100 -> WindowInfo.WindowType.Medium
            else -> WindowInfo.WindowType.Expanded
        },
        screenWidthInfo = when {
            configuration.screenWidthDp < 460 -> WindowInfo.WindowType.Compact
            configuration.screenWidthDp < 850 -> WindowInfo.WindowType.Medium
            else -> WindowInfo.WindowType.Expanded
        },
        screenHeight = configuration.screenHeightDp.dp,
        screenWidth = configuration.screenWidthDp.dp
    )

}

data class WindowInfo(
    val screenHeightInfo: WindowType,
    val screenWidthInfo: WindowType,
    val screenHeight: Dp,
    val screenWidth: Dp,
) {
    sealed class WindowType {
        data object Compact: WindowType()
        data object Medium: WindowType()
        data object Expanded: WindowType()
    }
}