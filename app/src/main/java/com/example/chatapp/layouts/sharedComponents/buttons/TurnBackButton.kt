package com.example.chatapp.layouts.sharedComponents.buttons

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.example.chatapp.differentScreensSupport.sdp

@Composable
fun TurnBackButton(
    modifier: Modifier = Modifier.size(40.sdp),
    iconPadding: Dp = 6.sdp,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier
                .padding(iconPadding)
                .fillMaxSize(),
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "turn back",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}