package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents

import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.chatapp.differentScreensSupport.sdp

@Composable
fun ScrollDownButton(
    modifier: Modifier,
    onClick: () -> Unit,
    unseenMessagesCount: Int,
) {
    Log.d("recomposition","COMPOSE!")

    Box(
        modifier = modifier
            .padding(12.sdp)
            .size(48.sdp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
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

        if(unseenMessagesCount != 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(16.sdp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.error)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = unseenMessagesCount.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }

}