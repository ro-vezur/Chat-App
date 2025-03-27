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
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.paging.compose.itemKey
import com.example.chatapp.Dtos.chat.ChatItem
import com.example.chatapp.Dtos.chat.LocalChatInfo
import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.Dtos.chat.chatType.ChatType
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.HH_MM
import com.example.chatapp.LocalUser
import com.example.chatapp.differentScreensSupport.sdp
import com.example.chatapp.helpers.time.formatLastOnlineTime
import com.example.chatapp.helpers.time.getDateFromMillis
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.oneToOneChat.viewmodel.OneToOneChatUiState
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.oneToOneChat.viewmodel.OneToOneChatViewModelEvent
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents.DateHeader
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents.MessageStatusIcons.CheckedStatusIcon
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents.MessageStatusIcons.NoneStatusIcon
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents.MessageStatusIcons.ReceivedStatusIcon
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents.ScrollDownButton
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents.SharedChatsBottomBar
import com.example.chatapp.layouts.sharedComponents.buttons.TurnBackButton
import com.example.chatapp.layouts.sharedComponents.images.UserImage
import com.example.chatapp.others.states.DirectionalLazyListState.ScrollDirection
import com.example.chatapp.others.states.DirectionalLazyListState.rememberDirectionalLazyListState
import com.example.chatapp.ui.theme.ChatAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

@Composable
fun OneToOneChatScreen(
    chatUiState: OneToOneChatUiState,
    paginatedMessages: LazyPagingItems<ChatItem>,
    navController: NavController,
    dispatchEvent: (OneToOneChatViewModelEvent) -> Unit,
) {
    val mainUser = LocalUser.current

    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState( )
    val directionalLazyListState = rememberDirectionalLazyListState(listState = lazyListState)

    val lastItemIndex by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex }
    }
    val visibleItemsInfo by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex }
    }

    var isFirstScrollDownLaunchedEffectCall by rememberSaveable {
        mutableStateOf(true)
    }
    var visibleDateHeader by remember { mutableStateOf<String?>(null) }
    var visibleScrollDownButton by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = visibleItemsInfo, key2 = chatUiState.chat.lastMessageId) {
        if(paginatedMessages.itemCount != 0 && lazyListState.layoutInfo.visibleItemsInfo.isNotEmpty()) {
            val firstVisibleItem = paginatedMessages[lazyListState.layoutInfo.visibleItemsInfo.first().index]

            lazyListState.layoutInfo.visibleItemsInfo.forEach { item ->
                val chatItem = paginatedMessages[item.index]

                chatItem?.let {
                    if(chatItem is ChatItem.MessageItem && chatItem.message.userId != mainUser.id) {
                        dispatchEvent(OneToOneChatViewModelEvent.AddMessageToReadList(chatItem.message,mainUser.id))
                    }
                }
            }

            firstVisibleItem?.let { chatItem ->
                if(chatItem is ChatItem.MessageItem) {

                    if(chatItem.message.id == chatUiState.chat.lastMessageId) {
                        dispatchEvent(OneToOneChatViewModelEvent.UpdateUserLastSeenMessage(mainUser.id,chatItem.message.id))
                    }
                }
            }

            dispatchEvent(OneToOneChatViewModelEvent.SetMessagesReadStatus(mainUser.id))
        }
    }

    LaunchedEffect(key1 = paginatedMessages.itemCount,key2 = lastItemIndex) {
        if(isFirstScrollDownLaunchedEffectCall) {
            isFirstScrollDownLaunchedEffectCall = false
            return@LaunchedEffect
        }

        if(paginatedMessages.itemCount != 0 && lastItemIndex <= 2) {
            val firstVisibleItem = paginatedMessages[0]

            Log.d("last item index",lastItemIndex.toString())
            lazyListState.scrollToItem(0)

            firstVisibleItem?.let { chatItem ->
                if(chatItem is ChatItem.MessageItem) {

                    if(chatItem.message.id == chatUiState.chat.lastMessageId) {
                        dispatchEvent(OneToOneChatViewModelEvent.UpdateUserLastSeenMessage(mainUser.id,chatUiState.chat.lastMessageId))
                    //    dispatchEvent(OneToOneChatViewModelEvent.AddMessageToReadList(chatItem.message,mainUser.id))
                    }
                }
            }
        }
    }


    LaunchedEffect(key1 = directionalLazyListState.scrollDirection,key2 = lazyListState.canScrollBackward) {

        visibleScrollDownButton = when {
            !lazyListState.canScrollBackward -> false
            directionalLazyListState.scrollDirection == ScrollDirection.NONE -> return@LaunchedEffect
            else -> {
                delay(100)
                Log.d("scroll orientation",directionalLazyListState.scrollDirection.name)
                directionalLazyListState.scrollDirection == ScrollDirection.UP
            }
        }
    }

    LaunchedEffect(key1 = lazyListState.isScrollInProgress) {
        if(!lazyListState.isScrollInProgress) {
            dispatchEvent(OneToOneChatViewModelEvent.SetMessagesReadStatus(mainUser.id))
        }
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
            .mapNotNull { visibleItems ->
                visibleItems.lastOrNull()?.let { lastItem ->
                    if(lastItem.index < paginatedMessages.itemCount) {
                        val item = paginatedMessages[lastItem.index]
                        when(item) {
                            is ChatItem.MessageItem -> item.message.formatDate()
                            is ChatItem.DateHeader -> ""
                            else -> ""
                        }
                    } else {
                        val lastItemInList = paginatedMessages[paginatedMessages.itemCount-1]
                        when(lastItemInList) {
                            is ChatItem.MessageItem -> lastItemInList.message.formatDate()
                            is ChatItem.DateHeader -> lastItemInList.date
                            else -> ""
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
                        text = if(chatUiState.user.onlineStatus.devices.isNotEmpty()) "Online" else  formatLastOnlineTime(chatUiState.user.onlineStatus.lastTimeSeen),
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
                    verticalArrangement = Arrangement.spacedBy(4.sdp),
                    state = lazyListState,
                ) {
                    val itemCount = paginatedMessages.itemCount



                    items(
                        count = itemCount,
                        key = paginatedMessages.itemKey { item -> when(item) {
                                is ChatItem.DateHeader -> item.date
                                is ChatItem.MessageItem -> item.message.id
                                is ChatItem.NewMessagesSeparator -> item.text
                            }
                        }
                    ) { index ->

                        val chatItem = paginatedMessages.peek(index)
                        val previousMessage =  if(index + 1 < itemCount) paginatedMessages[index+1] else null


                        chatItem?.let {
                            if(chatItem is ChatItem.DateHeader ) {
                                DateHeader(date = chatItem.date)
                            }
                            
                            if (chatItem is ChatItem.NewMessagesSeparator) {
                                DateHeader(date = chatItem.text)
                            }

                            if(chatItem is ChatItem.MessageItem) {

                                val isPreviousMessageBySameUser = when(previousMessage) {
                                    is ChatItem.MessageItem -> { previousMessage.message.userId == chatItem.message.userId }
                                    else -> false
                                }

                                if(chatItem.message.userId == mainUser.id) {
                                    MyMessageCard(
                                        message = chatItem.message,
                                        oppositeUser = chatUiState.user,
                                        isPreviousMessageBySameUser = isPreviousMessageBySameUser
                                    )
                                } else {
                                    OppositeUserMessageCard(
                                        message = chatItem.message,
                                        isPreviousMessageBySameUser = isPreviousMessageBySameUser,
                                        chatUiState = chatUiState,
                                    )
                                }
                            }
                        }
                    }
                }

                visibleDateHeader?.let {
                    DateHeader(date = it)
                }

                if(visibleScrollDownButton) {
                    ScrollDownButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd),
                        onClick = {
                            scope.launch {
                                lazyListState.scrollToItem(0)
                            }
                        }
                    )
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
                sendMessage = { message ->
                    dispatchEvent(OneToOneChatViewModelEvent.SendMessage(message))
                    scope.launch {
                        lazyListState.scrollToItem(0)
                    }
                },
                addLocalChat = {
                    val localChatInfo = LocalChatInfo(
                        id = chatUiState.chat.id,
                        chatType = ChatType.USER
                    )

                    dispatchEvent(OneToOneChatViewModelEvent.AddLocalChatInfo(mainUser.id,localChatInfo))
                    dispatchEvent(OneToOneChatViewModelEvent.AddLocalChatInfo(chatUiState.user.id,localChatInfo))
                }
            )
        }
    }
}

@Composable
private fun MyMessageCard(
    modifier: Modifier = Modifier,
    message: Message,
    oppositeUser: User,
    isPreviousMessageBySameUser: Boolean,
) {
    val mainUser = LocalUser.current

    Box(
        modifier = modifier
            .padding(top = if (isPreviousMessageBySameUser) 0.sdp else 5.sdp, start = 80.sdp)
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Column (
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
                .padding(12.sdp),
            verticalArrangement = Arrangement.spacedBy(4.sdp),
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 4.sdp),
                text = message.content,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodyMedium
            )

            Row (
                modifier = Modifier
                    .padding(end = 2.sdp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {

                Text(
                    text = message.sentTimeStamp?.let {  getDateFromMillis(message.sentTimeStamp,HH_MM) } ?: "",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodySmall
                )
                
                Spacer(modifier = Modifier.width(5.sdp))

                when {
                    message.seenBy.contains(oppositeUser.id) -> { CheckedStatusIcon() }
                    oppositeUser.onlineStatus.devices.isNotEmpty() -> { ReceivedStatusIcon() }
                    oppositeUser.blockedUsers.contains(mainUser.id) -> { NoneStatusIcon() }
                    else -> { NoneStatusIcon() }
                }
            }
        }
    }
}

@Composable
private fun OppositeUserMessageCard(
    modifier: Modifier = Modifier,
    message: Message,
    isPreviousMessageBySameUser: Boolean,
    chatUiState: OneToOneChatUiState,
) {
    Row(
        modifier = modifier
            .padding(top = if (isPreviousMessageBySameUser) 0.sdp else 5.sdp, end = 80.sdp)
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

        Column (
            modifier = Modifier
                .padding(top = if (isPreviousMessageBySameUser) 0.sdp else 5.sdp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(
                        topStart = if (isPreviousMessageBySameUser) 10.sdp else 0.sdp,
                        topEnd = 10.sdp,
                        bottomStart = 10.sdp,
                        bottomEnd = 10.sdp,
                    )
                )
                .padding(10.sdp),
            verticalArrangement = Arrangement.spacedBy(5.sdp)
        ) {
            Text(
                text = message.content,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )

            Row (
                modifier = Modifier
                    .padding(start = 2.sdp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {

                Text(
                    text = message.sentTimeStamp?.let {  getDateFromMillis(message.sentTimeStamp,HH_MM) } ?: "",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodySmall
                )
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

val fakeMessages = mutableListOf<ChatItem>(
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