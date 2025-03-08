package com.example.chatapp.layouts.verticalLayout.loggedScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatapp.Dtos.chat.ChatUI
import com.example.chatapp.differentScreensSupport.sdp
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chats.viewmodel.ChatsUiState
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chats.viewmodel.ChatsViewModelEvent
import com.example.chatapp.layouts.sharedComponents.images.UserImage
import com.example.chatapp.layouts.sharedComponents.resultScreens.LoadingScreen
import com.example.chatapp.others.Resource
import com.example.chatapp.ui.theme.ChatAppTheme

@Composable
fun VerticalChatsScreen(
    chatsUiState: ChatsUiState,
    dispatchEvent: (ChatsViewModelEvent) -> Unit,
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {

        when(chatsUiState.chats) {
            is Resource.Error -> {}
            is Resource.Loading -> { LoadingScreen(Modifier.fillMaxSize()) }
            is Resource.Success -> {
                chatsUiState.chats.data?.let { chats ->
                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = 6.sdp),
                        verticalArrangement = Arrangement.spacedBy(10.sdp)
                    ) {
                        items(
                            items = chats,
                            key = { chat -> chat.id }
                        ) { chat ->
                            ChatCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(70.sdp)
                                    .clip(RoundedCornerShape(20))
                                    .clickable {

                                    },
                                chatUI = chat
                            )
                        }
                    }
                }
            }
        }


    }
}

@Composable
fun ChatCard(
    modifier: Modifier = Modifier,
    chatUI: ChatUI
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        UserImage(
            modifier = Modifier
                .padding(horizontal = 8.sdp)
                .size(60.sdp)
                .clip(CircleShape),
            imageUrl = chatUI.imageUrl
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 8.sdp, vertical = 6.sdp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = chatUI.name.toString(),
                style = typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(10.sdp))

            Text(
                text = chatUI.lastMessage,
                style = typography.bodyMedium,
                color = colorScheme.onSecondary,
                fontWeight = FontWeight.SemiBold
            )

        }
    }

}

@Preview
@Composable
private fun previewPhoneChats() {
    ChatAppTheme(
        darkTheme = false
    ) {
        VerticalChatsScreen(
            chatsUiState = ChatsUiState(),
            dispatchEvent = {}
        )
    }
}

