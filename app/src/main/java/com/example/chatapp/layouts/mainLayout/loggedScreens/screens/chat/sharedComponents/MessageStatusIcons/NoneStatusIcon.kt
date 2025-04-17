package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents.MessageStatusIcons

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.chatapp.differentScreensSupport.sdp

@Composable
fun NoneStatusIcon() {
    Icon(
        modifier = Modifier
            .size(15.sdp),
        imageVector = Icons.Filled.Check,
        contentDescription = "Check",
        tint = Color.White
        )
}