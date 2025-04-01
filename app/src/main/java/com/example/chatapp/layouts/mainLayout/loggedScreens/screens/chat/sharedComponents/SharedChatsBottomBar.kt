package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents

import android.util.Log
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.LocalUser
import com.example.chatapp.differentScreensSupport.sdp
import com.example.chatapp.helpers.time.getCurrentTimeInMillis
import kotlinx.coroutines.delay
import java.util.UUID

@Composable
fun SharedChatsBottomBar(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    sendMessage: (Message) -> Unit,
    addLocalChat: () -> Unit,
    isUserTyping: (Boolean) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var sendMessageDebounce by rememberSaveable {
        mutableStateOf(false)
    }

    val isFocused by interactionSource.collectIsFocusedAsState()

    val mainUser = LocalUser.current

    LaunchedEffect(key1 = sendMessageDebounce) {
        if(sendMessageDebounce) {
            delay(1000)
            sendMessageDebounce = false
        }
    }

    LaunchedEffect(key1 = isFocused,key2 = value) {
        isUserTyping(isFocused && value.isNotEmpty())
    }

    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.weight(0.4f))

        TextField(
            modifier = Modifier
                .heightIn(50.sdp, Int.MAX_VALUE.sdp)
                .weight(4f)
                .onFocusChanged { focusState ->
                    Log.d("is captured", focusState.isCaptured.toString())
                    Log.d("is focused?", focusState.isFocused.toString())

                    if (focusState.isFocused) {

                    } else {

                    }
                }
                .focusable(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
            ),
            textStyle = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal),
            placeholder = {
                Text(
                    text = "Message...",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal),
                )
            },
            value = value,
            onValueChange = onValueChange,
            interactionSource = interactionSource,
        )

        if(value.isBlank()) {
            IconButton(
                modifier = Modifier,
                onClick = {

                }
            ) {
                Icon(
                    modifier = Modifier
                        .size(28.sdp),
                    imageVector = Icons.Filled.AttachFile,
                    contentDescription = "attach file"
                )
            }

            IconButton(
                onClick = {

                }
            ) {
                Icon(
                    modifier = Modifier
                        .size(28.sdp),
                    imageVector = Icons.Filled.Mic,
                    contentDescription = "attach file"
                )
            }
        } else {
            IconButton(
                modifier = Modifier
                    .padding(horizontal = 10.sdp)
                    .size(40.sdp)
                    .clip(CircleShape),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                ),
                onClick = {
                    val uid = UUID.randomUUID()
                    val message = Message(
                        id = uid.toString(),
                        userId = mainUser.id,
                        content = value,
                        sentTimeStamp = getCurrentTimeInMillis(),
                    )

                    if(!sendMessageDebounce) {
                        sendMessageDebounce = true
                        onValueChange("")
                        sendMessage(message)
                        addLocalChat()
                    }

                }
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.sdp),
                    imageVector = Icons.Filled.Send,
                    contentDescription = "attach file"
                )
            }
        }
    }
}