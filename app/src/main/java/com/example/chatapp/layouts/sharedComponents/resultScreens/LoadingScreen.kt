package com.example.chatapp.layouts.sharedComponents.resultScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import com.example.chatapp.differentScreensSupport.sdp

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize(0.15f),
            strokeWidth = 5.sdp,
            color = colorScheme.primary,
            trackColor = colorScheme.secondary,
            strokeCap = StrokeCap.Butt
        )
    }
}