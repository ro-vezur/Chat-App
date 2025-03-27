package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.chatapp.differentScreensSupport.sdp

@Composable
fun ScrollDownButton(
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .padding(12.sdp)
            .size(44.sdp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.background)
            .border(
                BorderStroke(1.sdp, MaterialTheme.colorScheme.surface),
                CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier
                .size(32.sdp),
            imageVector = Icons.Filled.ArrowDownward,
            contentDescription = "arrow downward",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}