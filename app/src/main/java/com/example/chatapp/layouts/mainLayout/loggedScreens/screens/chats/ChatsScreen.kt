package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.chatapp.Dtos.chat.ChatUI
import com.example.chatapp.Dtos.chat.enums.ChatType
import com.example.chatapp.LocalUser
import com.example.chatapp.differentScreensSupport.sdp
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chats.viewmodel.ChatsUiState
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chats.viewmodel.ChatsViewModelEvent
import com.example.chatapp.layouts.sharedComponents.inputFields.CustomSearchBar
import com.example.chatapp.layouts.sharedComponents.resultScreens.LoadingScreen
import com.example.chatapp.navigation.ScreenRoutes
import com.example.chatapp.ui.theme.ChatAppTheme
import kotlinx.coroutines.flow.flowOf

@Composable
fun ChatsScreen(
    chatsUiState: ChatsUiState,
    paginatedUserChats: LazyPagingItems<ChatUI>,
    dispatchEvent: (ChatsViewModelEvent) -> Unit,
) {
    val mainUser = LocalUser.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        CustomSearchBar(
            query = chatsUiState.searchQuery,
            placeHolder = "Enter Chat's Name to Find",
            onSearchQueryChange = { query ->
                dispatchEvent(ChatsViewModelEvent.UpdateSearchField(query))
            }
        )

        when(paginatedUserChats.loadState.refresh) {
            is LoadState.Loading -> LoadingScreen(Modifier.fillMaxSize())
            is LoadState.Error -> {}
            is LoadState.NotLoading -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 6.sdp),
                    verticalArrangement = Arrangement.spacedBy(10.sdp)
                ) {
                    val sortedChats = paginatedUserChats.itemSnapshotList
                        .mapNotNull { chat -> chat }
                        .sortedByDescending { it.lastMessage.sentTimeStamp }

                    item { }

                    items(
                        sortedChats,
                        key = { it.id }
                    ) { chatUI ->
                        ChatUICard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.sdp)
                                .clickable {
                                    when (chatUI.chatType) {
                                        ChatType.GROUP -> {

                                        }
                                        ChatType.USER -> {
                                            dispatchEvent(
                                                ChatsViewModelEvent.NavigateTo(
                                                    "${ScreenRoutes.LoggedScreens.OneToOneChatRoute.MAIN_ROUTE_PART}/${chatUI.id}/${chatUI.user?.id}"
                                                )
                                            )
                                        }
                                    }
                                },
                            chatUI = chatUI
                        )
                    }

                    item { }
                }
            }
        }
    }

}
@Preview
@Composable
private fun previewPhoneChats() {
    ChatAppTheme(
        darkTheme = false
    ) {
        ChatsScreen(
            chatsUiState = ChatsUiState(),
            paginatedUserChats = flowOf(PagingData.from(listOf<ChatUI>())).collectAsLazyPagingItems(),
            dispatchEvent = {}
        )
    }
}