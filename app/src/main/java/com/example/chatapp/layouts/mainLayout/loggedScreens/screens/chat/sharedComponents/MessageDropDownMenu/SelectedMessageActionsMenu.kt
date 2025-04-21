package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents.MessageDropDownMenu

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.LocalUser
import com.example.chatapp.differentScreensSupport.sdp
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.oneToOneChat.viewmodel.OneToOneChatViewModelEvent
import kotlinx.coroutines.Job

@Composable
fun SelectedMessageActionsMenu(
    selectedMessageSize: IntSize,
    selectedMessageOffset: Offset,
    selectedMessage: Message,
    onDismiss: () -> Unit,
    dispatchEvent: (OneToOneChatViewModelEvent) -> Job,
) {
    val mainUser = LocalUser.current

    var selectedMessageActionsMenuSize by remember { mutableStateOf(IntSize.Zero) }
    val actionsList by remember {
        mutableStateOf(
            if(selectedMessage.userId == mainUser.id) MessageDropDownMenuButtonAction.actionsOverMyMessages
            else MessageDropDownMenuButtonAction.actionsOverOtherMessages
        )
    }
    val offset by remember(selectedMessageOffset,selectedMessageSize,selectedMessageActionsMenuSize) {
        mutableStateOf(
            if(selectedMessage.userId == mainUser.id) {
                IntOffset(
                    x = selectedMessageOffset.x.toInt() + ((selectedMessageSize.width / 2) - (selectedMessageActionsMenuSize.width)),
                    y = selectedMessageOffset.y.toInt() + ((selectedMessageSize.height / 2) - (selectedMessageActionsMenuSize.height / 2))
                )
            } else {
                Log.d("selectedMessageActionsMenuSize",selectedMessageActionsMenuSize.height.toString())
                IntOffset(
                    x = selectedMessageSize.width / 2
                    //- ((selectedMessageSize.width / 2) + (selectedMessageActionsMenuSize.width))
                    ,
                    y = selectedMessageOffset.y.toInt() + ((selectedMessageSize.height / 2) - (selectedMessageActionsMenuSize.height / 2))
                )
            }
        )
    }

    Popup(
        offset = IntOffset.Zero,
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.25f)))
    }

    Popup(
        offset = offset,
        properties = PopupProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            clippingEnabled = true
        ),
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .width(180.sdp)
                .height(260.sdp)
                .background(MaterialTheme.colorScheme.surface)
                .onSizeChanged {
                    selectedMessageActionsMenuSize = it
                }
        ) {

            LazyColumn(
                modifier = Modifier
                    .height(200.sdp)
                    .padding(vertical = 8.sdp),
                verticalArrangement = Arrangement.spacedBy(12.sdp)
            ) {
                items(
                    actionsList
                ) { action ->
                    SelectedMessageMenuActionCard(
                        modifier = Modifier
                            .padding(horizontal = 15.sdp, vertical = 2.sdp)
                            .fillMaxWidth()
                            .clickable {
                                when (action) {
                                    MessageDropDownMenuButtonAction.DELETE -> {
                                        dispatchEvent(
                                            OneToOneChatViewModelEvent.DeleteMessage(
                                                selectedMessage.id,
                                                selectedMessage.chatId
                                            )
                                        )
                                    }

                                    MessageDropDownMenuButtonAction.EDIT -> {
                                        dispatchEvent(
                                            OneToOneChatViewModelEvent.ChangeEditModeState(
                                                selectedMessage
                                            )
                                        )
                                        dispatchEvent(
                                            OneToOneChatViewModelEvent.ChangeSendMessageText(
                                                selectedMessage.content
                                            )
                                        )
                                    }
                                }
                            },
                        action = action,
                    )
                }
            }
        }
    }
}

@Composable
fun SelectedMessageMenuActionCard(
    modifier: Modifier,
    action: MessageDropDownMenuButtonAction
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.sdp)
    ) {
        Icon(
            modifier = Modifier
                .size(28.sdp),
            imageVector = action.icon,
            contentDescription = "action icon"
        )

        Text(
            modifier = Modifier,
            text = action.title,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal)
        )
    }
}
