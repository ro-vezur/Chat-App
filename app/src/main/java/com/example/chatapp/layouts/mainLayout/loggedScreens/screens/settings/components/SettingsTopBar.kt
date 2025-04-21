package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.chatapp.Dtos.user.userSettings.SettingsSelectionValueVariants
import com.example.chatapp.differentScreensSupport.sdp

@Composable
fun SettingsTopBar(
    modifier: Modifier = Modifier,
    header: String,
    turnBack: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.sdp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 5.sdp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.sdp)
    ) {
        IconButton(
            onClick = turnBack
        ) {
            Icon(
                modifier = Modifier
                    .size(30.sdp),
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "turn back",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Text(
            text = header,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}