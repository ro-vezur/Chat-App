package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatapp.LocalUser
import com.example.chatapp.differentScreensSupport.checkIsPortrait
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chats.viewmodel.ChatsUiState
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chats.viewmodel.ChatsViewModelEvent
import com.example.chatapp.layouts.verticalLayout.loggedScreens.VerticalChatsScreen
import com.example.chatapp.ui.theme.ChatAppTheme

@Composable
fun ChatsScreen(
    chatsUiState: ChatsUiState,
    dispatchEvent: (ChatsViewModelEvent) -> Unit,
) {
    val mainUser = LocalUser.current

    LaunchedEffect(key1 = mainUser) {
        dispatchEvent(ChatsViewModelEvent.FetchUserChats(mainUser.localChats))
    }

    if(checkIsPortrait()) {
        VerticalChatsScreen(
            chatsUiState = chatsUiState,
            dispatchEvent = dispatchEvent,

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
            chatsUiState = ChatsUiState(  ),
            dispatchEvent = {}
        )
    }
}