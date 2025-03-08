package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.chatapp.differentScreensSupport.sdp

@Composable
fun DateHeader(date: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.sdp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}