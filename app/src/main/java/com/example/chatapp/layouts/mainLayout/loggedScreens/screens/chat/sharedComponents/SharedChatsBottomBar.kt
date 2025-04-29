package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.LocalUser
import com.example.chatapp.differentScreensSupport.sdp
import com.example.chatapp.helpers.time.getCurrentTimeInMillis
import java.util.UUID

@Composable
fun SharedChatsBottomBar(
    modifier: Modifier = Modifier,
    typedText: String,
    messageToEdit: Message?,
    selectedImagesToSend: List<Uri>,
    changeSendMessageText: (String) -> Unit,
    sendMessage: (Message) -> Unit,
    editMessage: (Message) -> Unit,
    addLocalChat: () -> Unit,
    isUserTyping: (Boolean) -> Unit,
    changeMessageToEdit: (Message?) -> Unit,
    onSelectMedia: (List<Uri>) -> Unit,
    removeMediaFromSelection: (Uri) -> Unit,
    sendMedia: () -> Unit,
) {
    val rememberActivityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = onSelectMedia
    )
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val mainUser = LocalUser.current

    LaunchedEffect(key1 = isFocused,key2 = typedText) {
        isUserTyping(isFocused && typedText.isNotEmpty())
    }

    Column(
        modifier = modifier
    ) {
        if(messageToEdit != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.sdp),
            ) {
                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = {
                        changeMessageToEdit(null)
                        changeSendMessageText("")
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(25.sdp),
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "turn of edit mode"
                    )
                }
            }

            HorizontalDivider(
                thickness = 1.sdp,
                color = MaterialTheme.colorScheme.surface
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(55.sdp, 250.sdp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if(selectedImagesToSend.isEmpty()) {
                TextField(
                    modifier = Modifier
                        .padding(start = 8.sdp)
                        .heightIn(50.sdp, Int.MAX_VALUE.sdp)
                        .weight(1f)
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
                    value = typedText,
                    onValueChange = changeSendMessageText,
                    interactionSource = interactionSource,
                )

                when {
                    typedText.isBlank() -> {
                        IconButton(
                            modifier = Modifier,
                            onClick = {
                                rememberActivityResultLauncher.launch(
                                    PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(28.sdp),
                                imageVector = Icons.Filled.Photo,
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
                                contentDescription = "micro"
                            )
                        }
                    }

                    messageToEdit == null -> {
                        ActionButtonCard(
                            background = MaterialTheme.colorScheme.primary,
                            icon = Icons.Filled.Send,
                            onClick = {
                                val uid = UUID.randomUUID()
                                val message = Message(
                                    id = uid.toString(),
                                    userId = mainUser.id,
                                    content = typedText,
                                    sentTimeStamp = getCurrentTimeInMillis(),
                                )

                                changeSendMessageText("")
                                sendMessage(message)
                                addLocalChat()
                            }
                        )
                    }

                    else -> {
                        ActionButtonCard(
                            background = MaterialTheme.colorScheme.primary,
                            icon = Icons.Filled.Check,
                            enabled = typedText != messageToEdit.content,
                            onClick = {
                                val newMessage = messageToEdit.copy(
                                    content = typedText,
                                    edited = true
                                )

                                changeSendMessageText("")
                                editMessage(newMessage)
                                changeMessageToEdit(null)
                            }
                        )
                    }
                }
            } else {
                ActionButtonCard(
                    background = MaterialTheme.colorScheme.background,
                    icon = Icons.Filled.Add,
                    onClick = {
                        rememberActivityResultLauncher.launch(
                            input = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )

                LazyRow(
                    modifier = Modifier
                        .padding(vertical = 8.sdp)
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.sdp)
                ) {
                    items(selectedImagesToSend) { uri ->
                        AsyncImage(
                            modifier = Modifier
                                .size(60.sdp)
                                .clip(RoundedCornerShape(4.sdp))
                                .clickable {
                                    removeMediaFromSelection(uri)
                                },
                            contentScale = ContentScale.Crop,
                            model = uri,
                            contentDescription = "selected image"
                        )
                    }
                }

                ActionButtonCard(
                    background = MaterialTheme.colorScheme.primary,
                    icon = Icons.Filled.Send,
                    onClick = sendMedia
                )
            }
        }
    }
}

@Composable
private fun ActionButtonCard(
    background: Color,
    icon: ImageVector,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = Modifier
            .padding(horizontal = 10.sdp)
            .size(40.sdp)
            .clip(CircleShape),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = background,
            contentColor = Color.White,
        ),
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier
                .size(24.sdp),
            imageVector = icon,
            contentDescription = "send message"
        )
    }
}