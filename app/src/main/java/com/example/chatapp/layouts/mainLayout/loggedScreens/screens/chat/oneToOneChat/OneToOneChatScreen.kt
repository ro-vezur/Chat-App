package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.oneToOneChat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.chatapp.Dtos.chat.ChatItem
import com.example.chatapp.Dtos.chat.ChatType
import com.example.chatapp.Dtos.chat.LocalChatInfo
import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.LocalUser
import com.example.chatapp.differentScreensSupport.sdp
import com.example.chatapp.helpers.time.formatLastOnlineTime
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.oneToOneChat.viewmodel.OneToOneChatUiState
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.oneToOneChat.viewmodel.OneToOneChatViewModelEvent
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents.DateHeader
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents.SharedChatsBottomBar
import com.example.chatapp.layouts.sharedComponents.buttons.TurnBackButton
import com.example.chatapp.layouts.sharedComponents.images.UserImage
import com.example.chatapp.ui.theme.ChatAppTheme
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull

@Composable
fun OneToOneChatScreen(
    chatUiState: OneToOneChatUiState,
    paginatedMessages: LazyPagingItems<Any>,
    navController: NavController,
    dispatchEvent: (OneToOneChatViewModelEvent) -> Unit,
) {
    val mainUser = LocalUser.current

    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = paginatedMessages.itemCount) {
        if(paginatedMessages.itemCount != 0 && lazyListState.firstVisibleItemIndex == 0) {
            lazyListState.animateScrollToItem(0)
        }
    }

    var visibleDateHeader by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
            .mapNotNull { visibleItems ->
                visibleItems.lastOrNull()?.let { lastItem ->
                    if(lastItem.index < paginatedMessages.itemCount) {
                        val item = paginatedMessages[lastItem.index]
                        when(item) {
                            is ChatItem.MessageItem -> item.message.formatDate()
                            is ChatItem.DateHeader -> ""
                            else -> item.toString()
                        }
                    } else {
                        val lastItemInList = paginatedMessages[paginatedMessages.itemCount-1]
                        when(lastItemInList) {
                            is ChatItem.MessageItem -> lastItemInList.message.formatDate()
                            is ChatItem.DateHeader -> lastItemInList.date
                            else -> lastItemInList.toString()
                        }
                    }
                }
            }
            .collect { newDate -> visibleDateHeader = newDate }
    }

    Scaffold(
        modifier = Modifier,
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime),
        topBar = {
            Row(
                modifier = Modifier
                    .height(65.sdp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.tertiary),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TurnBackButton(
                    modifier = Modifier
                        .size(50.sdp),
                    iconPadding = 10.sdp,
                    navController = navController
                )

                UserImage(
                    modifier = Modifier
                        .padding(start = 14.sdp)
                        .size(40.sdp)
                        .clip(CircleShape),
                    imageUrl = chatUiState.user.imageUrl
                )

                Column(
                    modifier = Modifier
                        .padding(horizontal = 12.sdp)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.sdp)
                ) {
                    Text(
                        text = chatUiState.user.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )

                    Text(
                        text = if(chatUiState.user.fcmTokens.values.contains(true)) "Online" else  formatLastOnlineTime(chatUiState.user.seenLastTimeTimeStamp ?: 0),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                IconButton(
                    modifier = Modifier
                        .size(50.sdp),
                    onClick = {

                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(6.sdp)
                            .fillMaxSize(),
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "more vert",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()

        ) {
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.TopCenter
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    reverseLayout = true,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    state = lazyListState,
                ) {
                    val itemCount = paginatedMessages.itemCount

                    paginatedMessages.itemSnapshotList.forEachIndexed { index, any ->
                        val chatItem = paginatedMessages.peek(index)
                        val previousMessage =  if(index + 1 < itemCount) paginatedMessages[index+1] else null

                        chatItem?.let {
                            if(previousMessage is ChatItem.DateHeader && chatItem is ChatItem.MessageItem) {
                                item {
                                    DateHeader(date = chatItem.message.formatDate())
                                }
                            }
                            item {
                                if(chatItem is ChatItem.MessageItem) {

                                    if(chatItem.message.userId == mainUser.id) {
                                        MyMessageCard(
                                            message = chatItem.message
                                        )
                                    } else {
                                        OtherPeopleMessageCard(
                                            message = chatItem.message,
                                            isPreviousMessageBySameUser = when(previousMessage) {
                                                is ChatItem.MessageItem -> {
                                                    Log.d("previous msg",previousMessage.message.content)
                                                    previousMessage.message.userId == chatItem.message.userId
                                                }
                                                else -> false
                                            },
                                            chatUiState = chatUiState,
                                        )
                                    }
                                }
                            }

                        }
                    }
                }

                visibleDateHeader?.let {
                    DateHeader(date = it)
                }

            }

            SharedChatsBottomBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding()
                    .heightIn(65.sdp, 250.sdp)
                    .background(MaterialTheme.colorScheme.tertiary),
                value = chatUiState.sendMessageQuery,
                onValueChange = { query ->
                    dispatchEvent(OneToOneChatViewModelEvent.OnEnterQueryChange(query))
                },
                sendMessage = { message -> dispatchEvent(OneToOneChatViewModelEvent.SendMessage(message)) },
                addLocalChat = {
                    val localChatInfo = LocalChatInfo(
                        id = chatUiState.chat.id,
                        chatType = ChatType.USER
                    )

                    dispatchEvent(
                        OneToOneChatViewModelEvent.AddLocalChatInfo(
                            mainUser.id,localChatInfo.copy(name = chatUiState.user.name, imageUrl = chatUiState.user.imageUrl)
                        )
                    )

                    dispatchEvent(
                        OneToOneChatViewModelEvent.AddLocalChatInfo(
                            chatUiState.user.id,localChatInfo.copy(name = mainUser.name, imageUrl = mainUser.imageUrl)
                        )
                    )
                }
            )
        }
    }

    

}

@Composable
private fun MyMessageCard(
    modifier: Modifier = Modifier,
    message: Message
) {
    Row(
        modifier = modifier
            .padding(top = 5.sdp, start = 80.sdp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 6.sdp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(
                        topStart = 10.sdp,
                        topEnd = 0.sdp,
                        bottomStart = 10.sdp,
                        bottomEnd = 10.sdp,
                    )
                )
                .padding(12.sdp)
        ) {
            Text(
                text = message.content,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun OtherPeopleMessageCard(
    modifier: Modifier = Modifier,
    message: Message,
    isPreviousMessageBySameUser: Boolean,
    chatUiState: OneToOneChatUiState,
) {
    if(!isPreviousMessageBySameUser) {
        Spacer(modifier = Modifier.height(5.sdp))
    }

    Row(
        modifier = modifier
            .padding(end = 80.sdp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
    ) {

        Box(
            modifier = Modifier
                .padding(horizontal = 6.sdp)
                .size(40.sdp)
        ) {
            if(!isPreviousMessageBySameUser) {
                UserImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    imageUrl = chatUiState.user.imageUrl
                )
            }
        }

        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(
                        topStart = 10.sdp,
                        topEnd = 0.sdp,
                        bottomStart = 10.sdp,
                        bottomEnd = 10.sdp,
                    )
                )
                .padding(10.sdp)
        ) {
            Text(
                text = message.content,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview
@Preview
@Composable
private fun previewChatScreen() {
    ChatAppTheme(

    ) {
        OneToOneChatScreen(
            chatUiState = OneToOneChatUiState(
                user = User(name = "Tester ALLAHU AKBAR SYRIA ALAHU"),
                sendMessageQuery = "Hello"
            ),
            paginatedMessages = flowOf(PagingData.from(fakeMessages)).collectAsLazyPagingItems(),
            navController = rememberNavController(),
            dispatchEvent = {}
        )
    }
}

val fakeMessages = mutableListOf<Any>(
    ChatItem.DateHeader("2"),
    ChatItem.MessageItem(Message(id = "1", userId = "user_1", content = "Hey! How are you?", sentTimeStamp = 1708358460000)),
    ChatItem.MessageItem(Message(id = "2", userId = "user_2", content = "I'm good! What about you?", sentTimeStamp = 1708358460000)),
    ChatItem.MessageItem(Message(id = "3", userId = "user_1", content = "Doing great! Just working on a project.", sentTimeStamp = 1708358520000)),
    ChatItem.MessageItem(Message(id = "4", userId = "user_2", content = "Nice! What kind of project?", sentTimeStamp = 1708358580000)),
    ChatItem.MessageItem(Message(id = "5", userId = "user_1", content = "A chat app in Jetpack Compose!", sentTimeStamp = 1708358640000)),
    ChatItem.MessageItem(Message(id = "6", userId = "user_2", content = "Wow, that sounds awesome!", sentTimeStamp = 1708358700000)),
    ChatItem.MessageItem(Message(id = "7", userId = "user_1", content = "Yeah, but handling real-time updates is tricky.", sentTimeStamp = 1708358760000)),
    ChatItem.MessageItem(Message(id = "8", userId = "user_2", content = "I bet! Need any help?", sentTimeStamp = 1708358820000)),
    ChatItem.MessageItem(Message(id = "9", userId = "user_1", content = "Maybe! I'll let you know 😄", sentTimeStamp = 1708358880000)),
    ChatItem.MessageItem(Message(id = "10", userId = "user_2", content = "Alright, looking forward to seeing it!", sentTimeStamp = 1708358940000))
)