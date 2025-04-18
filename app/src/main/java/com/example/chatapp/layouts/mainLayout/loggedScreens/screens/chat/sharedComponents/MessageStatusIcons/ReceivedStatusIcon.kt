package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents.MessageStatusIcons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.chatapp.R
import com.example.chatapp.differentScreensSupport.sdp

@Composable
fun ReceivedStatusIcon() {
    Icon(
        modifier = Modifier
            .size(15.sdp),
        painter = painterResource(id = R.drawable.double_check),
        contentDescription = "Check",
        tint = Color.White
    )
}