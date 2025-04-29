package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.chatapp.Dtos.chat.ChatUI
import com.example.chatapp.Dtos.chat.enums.MessageType
import com.example.chatapp.differentScreensSupport.sdp
import com.example.chatapp.layouts.sharedComponents.images.UserImage

@Composable
fun ChatUICard(
    modifier: Modifier = Modifier,
    chatUI: ChatUI
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 8.sdp)
                .size(65.sdp)
        ) {
            UserImage(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(60.sdp)
                    .clip(CircleShape),
                imageUrl = chatUI.imageUrl
            )

            if(chatUI.unseenMessagesCount != 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(25.sdp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.error)
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center),
                        text = chatUI.unseenMessagesCount.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 8.sdp, vertical = 6.sdp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = chatUI.name.toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(10.sdp))

            Text(
                text = if(chatUI.typingUsersText.isNullOrBlank()) {
                    when(chatUI.lastMessage.messageType) {
                        MessageType.TEXT ->  chatUI.lastMessage.content
                        MessageType.IMAGE -> MessageType.IMAGE.title
                    }
                } else "${chatUI.typingUsersText}...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondary,
                maxLines = 1,
                fontWeight = FontWeight.SemiBold
            )

        }
    }
}