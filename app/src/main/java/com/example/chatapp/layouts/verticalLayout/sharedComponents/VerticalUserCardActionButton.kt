package com.example.chatapp.layouts.verticalLayout.sharedComponents

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.chatapp.differentScreensSupport.sdp

@Composable
fun UserCardActionButton(
    icon: ImageVector,
    colors: IconButtonColors,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = Modifier
            .padding(horizontal = 12.sdp)
            .size(40.sdp),
        colors = colors,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier
                .size(28.sdp),
            imageVector = icon,
            contentDescription = "Friend",
            tint = Color.White
        )
    }
}