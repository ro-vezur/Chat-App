package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chats

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatapp.fakeContacts
import com.example.chatapp.differentScreensSupport.checkIsPortrait
import com.example.chatapp.layouts.verticalLayout.loggedScreens.VerticalChatsScreen
import com.example.chatapp.ui.theme.ChatAppTheme

@Composable
fun ChatsScreen(
    chatsUiState: ChatsUiState,
) {

    if(checkIsPortrait()) {
        VerticalChatsScreen(
            chatsUiState = chatsUiState
        )
    } else {
        
    }

}
@Preview
@Composable
private fun previewPhoneChats() {
    ChatAppTheme(
        darkTheme = false
    ) {
        ChatsScreen(
            chatsUiState = ChatsUiState(
                contacts = fakeContacts
            )
        )
    }
}