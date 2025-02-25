package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.ui.theme.ChatAppTheme

@Composable
fun ChatScreen(
    chatUiState: ChatUiState,
) {

    var prompt by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        TextField(
            modifier = Modifier
                .padding(vertical = 22.dp),
            value = prompt,
            onValueChange = { prompt = it }
        )

        TextButton(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(65.dp),
            elevation = ButtonDefaults.elevatedButtonElevation(3.dp),
            onClick = {
            }
        ) {
            Text(text = "Generate Propmpt")
        }

        LazyColumn(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(chatUiState.messages) { message ->
                if(message.role == "user") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(40.dp, Int.MAX_VALUE.dp),
                        contentAlignment = Alignment.CenterEnd,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Text(
                                modifier = Modifier
                                    .weight(5f),
                                text = message.content,
                                textAlign = TextAlign.End
                            )

                            Text(
                                modifier = Modifier
                                    .weight(1f),
                                text = message.role,
                                textAlign = TextAlign.Center,
                            )
                        }

                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(60.dp, Int.MAX_VALUE.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Text(
                            modifier = Modifier
                                .weight(1f),
                            text = message.role,
                            textAlign = TextAlign.Center,
                        )
                            Text(
                                modifier = Modifier
                                    .weight(5f),
                                text = message.content
                            )
                        }

                    }
                }

            }
        }

    }

}

@Preview
@Preview
@Composable
private fun previewChatScreen() {
    ChatAppTheme(

    ) {
        ChatScreen(
            chatUiState = ChatUiState(
                messages = listOf(
                    Message(role = "user", content = "Hello AI!"),
                    Message(role = "AI", content = "Hi User!")
                )
            ),
        )
    }
}