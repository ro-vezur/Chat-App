package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatapp.differentScreensSupport.sdp
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents.MessageDropDownMenu.MessageDropDownMenuButtonAction
import com.example.chatapp.ui.theme.ChatAppTheme


@Composable
fun MessageDropDownMenu(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    isMyMessage: Boolean,
    changeExpandedState: (Boolean) -> Unit,
    dispatchAction: (MessageDropDownMenuButtonAction) -> Unit,
) {
    DropdownMenu(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        expanded = isExpanded,
        onDismissRequest = { changeExpandedState(false) }
    ) {
        if(isMyMessage) {
            MessageDropDownMenuButtonAction.actionsOverMyMessages.forEach { action ->
                ActionCard(
                    action = action,
                    dispatchAction = {
                        changeExpandedState(false)
                        dispatchAction(action)
                    }
                )
            }
        }
    }
}

@Composable
private fun ActionCard(
    action: MessageDropDownMenuButtonAction,
    dispatchAction: () -> Unit,
) {
    DropdownMenuItem(
        modifier = Modifier,
        text = {
            Text(
                modifier = Modifier,
                text = action.title,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        leadingIcon = {
            Icon(
                modifier = Modifier
                    .size(24.sdp),
                imageVector = action.icon,
                contentDescription = "action icon"
            )
        },
        onClick = dispatchAction
    )
}

@Preview
@Composable
private fun MessageDropDownMenuPREV() {
    ChatAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .fillMaxHeight(0.4f)
                ) {
                    Text(text = "Click me")

                    MessageDropDownMenu(
                        isExpanded = true,
                        isMyMessage = true,
                        changeExpandedState = {},
                        dispatchAction = {}
                    )
                }
            }
        }
    }
}