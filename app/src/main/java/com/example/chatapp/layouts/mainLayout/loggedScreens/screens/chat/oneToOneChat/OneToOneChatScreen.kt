package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.oneToOneChat

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.example.chatapp.Dtos.chat.ChatItem
import com.example.chatapp.Dtos.chat.LocalChatInfo
import com.example.chatapp.Dtos.chat.Message
import com.example.chatapp.Dtos.chat.enums.ChatType
import com.example.chatapp.Dtos.chat.enums.MessageType
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.Dtos.user.userSettings.SettingsSelectionValueVariants
import com.example.chatapp.HH_MM
import com.example.chatapp.LocalUser
import com.example.chatapp.differentScreensSupport.sdp
import com.example.chatapp.helpers.navigation.navigateBack
import com.example.chatapp.helpers.time.getDateFromMillis
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.oneToOneChat.viewmodel.OneToOneChatUiState
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.oneToOneChat.viewmodel.OneToOneChatViewModelEvent
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents.DateHeader
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents.MessageStatusIcons.CheckedStatusIcon
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents.MessageStatusIcons.NoneStatusIcon
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents.MessageStatusIcons.ReceivedStatusIcon
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents.ScrollDownButton
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents.SelectedMessageActionsMenu.SelectedMessageActionsMenu
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents.SharedChatsBottomBar
import com.example.chatapp.layouts.sharedComponents.buttons.TurnBackButton
import com.example.chatapp.layouts.sharedComponents.images.UserImage
import com.example.chatapp.ui.theme.ChatAppTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.job
import kotlinx.coroutines.launch

@Composable
fun OneToOneChatScreen(
    chatUiState: OneToOneChatUiState,
    sendMessageText: String,
    paginatedMessages: LazyPagingItems<ChatItem>,
    navController: NavController,
    dispatchEvent: (OneToOneChatViewModelEvent) -> Job,
) {
    Log.d("recomposition","COMPOSE!")

    val mainUser = LocalUser.current
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState( )


    val canScrollBackward by remember {
        derivedStateOf { lazyListState.canScrollBackward }
    }
    val lastItemIndex by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex }
    }
    val visibleItemsInfo by remember {
        derivedStateOf { lazyListState.layoutInfo.visibleItemsInfo }
    }

    var isFirstScrollDownLaunchedEffectCall by rememberSaveable {
        mutableStateOf(true)
    }
    var isFirstCheckLastMessagesLaunchedEffectCall by rememberSaveable {
        mutableStateOf(true)
    }
    var isCheckingLastMessages by rememberSaveable {
        mutableStateOf(false)
    }

    var selectedMessage by remember { mutableStateOf<Message?>(null) }
    var selectedMessageOffset by remember { mutableStateOf(Offset.Zero) }
    var selectedMessageSize by remember { mutableStateOf(IntSize.Zero) }

    var visibleDateHeader by remember { mutableStateOf<String?>(null) }
    val visibleScrollDownButton by remember(
        key1 = chatUiState.unseenMessagesCount,
        key2 = canScrollBackward
    ) {
        derivedStateOf {
            when {
                !canScrollBackward -> false
                lastItemIndex <= 1 -> false
                chatUiState.unseenMessagesCount != 0 -> true
                else -> { true }
            }
        }
    }
    var isOppositeUserTyping by remember { mutableStateOf(false) }

    BackHandler {
        if(selectedMessage != null) {
            selectedMessage = null
        } else {
            dispatchEvent(
                OneToOneChatViewModelEvent.RemoveUserTyping(
                    chatUiState.chat.id,
                    mainUser.id
                )
            )
            navController.navigateBack()
        }
    }

    LaunchedEffect(chatUiState.user) {
        val toast = Toast.makeText(context, "You are not allowed to chat this user", Toast.LENGTH_SHORT)

        when {
            chatUiState.user.settings.privacySettings.whoCanMessageMe == SettingsSelectionValueVariants.FRIENDS &&
                    !chatUiState.user.friends.contains(mainUser.id) -> {
                navController.navigateBack()
                toast.show()
            }
            chatUiState.user.settings.privacySettings.whoCanMessageMe == SettingsSelectionValueVariants.NOBODY -> {
                toast.show()
                navController.navigateUp()
            }
            chatUiState.user.blockedUsers.contains(mainUser.id) -> {
                navController.navigateBack()
                toast.show()
            }
            else -> return@LaunchedEffect
        }
    }

    LaunchedEffect(key1 = chatUiState.user.onlineStatus.devices) {
        if(chatUiState.user.onlineStatus.devices.isEmpty()) {
            dispatchEvent(OneToOneChatViewModelEvent.RemoveUserTyping(chatUiState.chat.id,chatUiState.user.id))
        }
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow { visibleItemsInfo.map { it.index } }
            .mapNotNull { visibleItemsIndexes ->
                visibleItemsIndexes.forEach { index ->
                    val chatItem = paginatedMessages[index]
                    chatItem?.let {
                        if(chatItem is ChatItem.MessageItem) {
                            dispatchEvent(OneToOneChatViewModelEvent.AddMessageToReadList(chatItem.message,mainUser.id))
                        }
                    }
                }

                visibleItemsIndexes.lastOrNull()?.let { lastItemIndex ->
                    if(lastItemIndex < paginatedMessages.itemCount) {
                        val item = paginatedMessages[lastItemIndex]
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
            .collect { newDate ->
                visibleDateHeader = newDate
            }
    }

    LaunchedEffect(key1 = chatUiState.usersTyping) {
        isOppositeUserTyping = when {
            chatUiState.usersTyping.size == 1 && chatUiState.usersTyping.contains(mainUser.id) -> false
            chatUiState.usersTyping.isEmpty() -> false
            else -> true
        }
    }


    LaunchedEffect(key1 = lazyListState) {
        snapshotFlow { lastItemIndex }
            .collectLatest { lastItemIndex ->
                if(lastItemIndex > 3 || isFirstCheckLastMessagesLaunchedEffectCall) {
                    isCheckingLastMessages = false
                }
        }

    }

    LaunchedEffect(key1 = paginatedMessages.itemCount,key2 = chatUiState.chat.lastReads[mainUser.id]) {
        if(paginatedMessages.itemCount != 0) {
            dispatchEvent(OneToOneChatViewModelEvent.SetUnseenMessagesCount(mainUser.id))
        }
    }


    LaunchedEffect(key1 = paginatedMessages.itemSnapshotList.firstOrNull()) {
        if(paginatedMessages.itemCount != 0 && isCheckingLastMessages) {
           for (index in 0..2) {
               if(paginatedMessages.itemCount > index) {
                   val chatItem = paginatedMessages[index]

                   if (chatItem is ChatItem.MessageItem) {
                       dispatchEvent(
                           OneToOneChatViewModelEvent.AddMessageToReadList(
                               chatItem.message,
                               mainUser.id
                           )
                       )
                   }
               }
           }

            delay(1000)
            dispatchEvent(OneToOneChatViewModelEvent.SetMessagesReadStatus(mainUser.id))
        }
    }

    LaunchedEffect(key1 = paginatedMessages.itemSnapshotList.firstOrNull()) {
        if(isFirstScrollDownLaunchedEffectCall) {
            isFirstScrollDownLaunchedEffectCall = false
            return@LaunchedEffect
        }

        if(paginatedMessages.itemCount != 0 && lastItemIndex <= 3) {
            lazyListState.animateScrollToItem(0)
        }
    }

    LaunchedEffect(key1 = lazyListState) {
        snapshotFlow { lazyListState.isScrollInProgress }
            .collectLatest { isScrollInProgress ->
                if(!isScrollInProgress && !isCheckingLastMessages) {
                    when {
                        isFirstCheckLastMessagesLaunchedEffectCall ->  isFirstCheckLastMessagesLaunchedEffectCall = false
                        lastItemIndex <= 3 -> isCheckingLastMessages = true
                    }

                    dispatchEvent(OneToOneChatViewModelEvent.SetMessagesReadStatus(mainUser.id))
                }
            }
    }

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopBar(
                modifier = Modifier,
                navController = navController,
                chatUiState = chatUiState,
                dispatchEvent = dispatchEvent,
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
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
                            .fillMaxWidth(),
                        reverseLayout = true,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.sdp),
                        state = lazyListState,
                    ) {
                        val itemCount = paginatedMessages.itemCount

                        items(
                            count = itemCount,
                            key = paginatedMessages.itemKey { item ->
                                when (item) {
                                    is ChatItem.DateHeader -> item.date
                                    is ChatItem.MessageItem -> item.message.id
                                    is ChatItem.NewMessagesSeparator -> item.text
                                }
                            }
                        ) { index ->

                            val chatItem = paginatedMessages.peek(index)
                            val previousMessage = if (index + 1 < itemCount) paginatedMessages[index + 1] else null

                            chatItem?.let {
                                if (chatItem is ChatItem.DateHeader) {
                                    DateHeader(date = chatItem.date)
                                }

                                if (chatItem is ChatItem.NewMessagesSeparator) {
                                    DateHeader(date = chatItem.text)
                                }

                                if (chatItem is ChatItem.MessageItem) {

                                    val isPreviousMessageBySameUser = when (previousMessage) {
                                        is ChatItem.MessageItem -> {
                                            previousMessage.message.userId == chatItem.message.userId
                                        }

                                        else -> false
                                    }

                                    if (chatItem.message.userId == mainUser.id) {
                                        when(chatItem.message.messageType) {
                                            MessageType.TEXT -> MyMessageCard(
                                                message = chatItem.message,
                                                oppositeUser = chatUiState.user,
                                                onMessageClick = { newAnchorOffset, newAnchorSize ->
                                                    selectedMessage = if(selectedMessage == null) {
                                                        chatItem.message
                                                    } else {
                                                        null
                                                    }
                                                    selectedMessageOffset = newAnchorOffset
                                                    selectedMessageSize = newAnchorSize
                                                }
                                            )
                                            MessageType.IMAGE -> MyImageMessageCard(
                                                message = chatItem.message,
                                                oppositeUser = chatUiState.user,
                                                onMessageClick = { newAnchorOffset, newAnchorSize ->
                                                    selectedMessage = if(selectedMessage == null) {
                                                        chatItem.message
                                                    } else {
                                                        null
                                                    }
                                                    selectedMessageOffset = newAnchorOffset
                                                    selectedMessageSize = newAnchorSize
                                                }
                                            )
                                        }
                                    } else {
                                        when(chatItem.message.messageType) {
                                            MessageType.TEXT -> OppositeUserMessageCard(
                                                message = chatItem.message,
                                                isPreviousMessageBySameUser = isPreviousMessageBySameUser,
                                                chatUiState = chatUiState,
                                                onMessageClick = { newAnchorOffset, newAnchorSize ->
                                                    selectedMessage = if(selectedMessage == null) {
                                                        chatItem.message
                                                    } else {
                                                        null
                                                    }
                                                    selectedMessageOffset = newAnchorOffset
                                                    selectedMessageSize = newAnchorSize
                                                }
                                            )
                                            MessageType.IMAGE -> OppositeUserImageMessageCard(
                                                message = chatItem.message,
                                                onMessageClick = { newAnchorOffset, newAnchorSize ->
                                                    selectedMessage = if(selectedMessage == null) {
                                                        chatItem.message
                                                    } else {
                                                        null
                                                    }
                                                    selectedMessageOffset = newAnchorOffset
                                                    selectedMessageSize = newAnchorSize
                                                }
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

                    if (visibleScrollDownButton) {
                        ScrollDownButton(
                            modifier = Modifier
                                .align(Alignment.BottomEnd),
                            onClick = {
                                scope.launch {
                                    lazyListState.animateScrollToItem(0)
                                }
                            },
                            unseenMessagesCount = chatUiState.unseenMessagesCount
                        )
                    }

                    if (isOppositeUserTyping) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = 12.sdp, bottom = 5.sdp)
                                .clip(RoundedCornerShape(6.sdp))
                                .background(MaterialTheme.colorScheme.surface)
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(7.sdp),
                                text = "${chatUiState.user.name} Is Typing...",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                when {
                    mainUser.settings.privacySettings.whoCanMessageMe == SettingsSelectionValueVariants.NOBODY -> return@Box
                    mainUser.settings.privacySettings.whoCanMessageMe == SettingsSelectionValueVariants.FRIENDS &&
                            !mainUser.friends.contains(chatUiState.user.id) -> return@Box
                    mainUser.blockedUsers.contains(chatUiState.user.id) -> return@Box
                    else -> {
                        SharedChatsBottomBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .navigationBarsPadding()
                                .imePadding()
                                .background(MaterialTheme.colorScheme.tertiary),
                            typedText = sendMessageText,
                            messageToEdit = chatUiState.messageToEdit,
                            selectedImagesToSend = chatUiState.selectedImagesToSend,
                            changeSendMessageText = { query ->
                                dispatchEvent(OneToOneChatViewModelEvent.ChangeSendMessageText(query))
                            },
                            sendMessage = { message ->
                                dispatchEvent(OneToOneChatViewModelEvent.SendMessage(message))
                                if (lastItemIndex > 3) {
                                    scope.launch {
                                        lazyListState.animateScrollToItem(0)
                                    }
                                }
                            },
                            editMessage = { message ->
                                dispatchEvent(OneToOneChatViewModelEvent.ConfirmMessageChanges(message))
                            },
                            addLocalChat = {
                                val localChatInfo = LocalChatInfo(
                                    id = chatUiState.chat.id,
                                    chatType = ChatType.USER
                                )

                                dispatchEvent(
                                    OneToOneChatViewModelEvent.AddLocalChatInfo(
                                        mainUser.id,
                                        localChatInfo
                                    )
                                )
                                dispatchEvent(
                                    OneToOneChatViewModelEvent.AddLocalChatInfo(
                                        chatUiState.user.id,
                                        localChatInfo
                                    )
                                )
                            },
                            isUserTyping = { isTyping ->
                                if (isTyping) {
                                    dispatchEvent(
                                        OneToOneChatViewModelEvent.AddUserTyping(
                                            chatUiState.chat.id,
                                            mainUser.id
                                        )
                                    )
                                } else {
                                    dispatchEvent(
                                        OneToOneChatViewModelEvent.RemoveUserTyping(
                                            chatUiState.chat.id,
                                            mainUser.id
                                        )
                                    )
                                }
                            },
                            changeMessageToEdit = { messageToEdit ->
                                dispatchEvent(
                                    OneToOneChatViewModelEvent.ChangeEditModeState(messageToEdit)
                                )
                            },
                            onSelectMedia = { uris ->
                                dispatchEvent(OneToOneChatViewModelEvent.AddImagesToSend(uris))
                            },
                            removeMediaFromSelection = { media ->
                                dispatchEvent(OneToOneChatViewModelEvent.RemoveMediaFromSelection(media))
                            },
                            sendMedia = {
                                dispatchEvent(OneToOneChatViewModelEvent.SendMedia)
                            }
                        )
                    }
                }
            }

            selectedMessage?.let {
                SelectedMessageActionsMenu(
                    selectedMessageOffset = selectedMessageOffset,
                    selectedMessageSize = selectedMessageSize,
                    selectedMessage = it,
                    onDismiss = { selectedMessage = null },
                    dispatchEvent = { event ->
                        selectedMessage = null
                        dispatchEvent(event)
                    }
                )
            }
        }
    }
}

@Composable
fun TopBar(
    modifier: Modifier,
    navController: NavController,
    chatUiState: OneToOneChatUiState,
    dispatchEvent: (OneToOneChatViewModelEvent) -> Job,
) {
    val mainUser = LocalUser.current

    Row(
        modifier = modifier
            .height(65.sdp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TurnBackButton(
            modifier = Modifier
                .size(50.sdp),
            iconPadding = 10.sdp,
            onClick = {
                navController.navigateBack()
                dispatchEvent(OneToOneChatViewModelEvent.RemoveUserTyping(chatUiState.chat.id,mainUser.id))
            }
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
                text = chatUiState.user.getOppositeUserOnlineStatus(mainUser.id) ?: "No Activity",
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
}
@Composable
private fun MyMessageCard(
    modifier: Modifier = Modifier,
    message: Message,
    oppositeUser: User,
    onMessageClick: (anchorOffset: Offset, anchorSize: IntSize) -> Unit
) {
    val mainUser = LocalUser.current

    val interactionSource = remember { MutableInteractionSource() }

    var anchorOffset by remember { mutableStateOf(Offset.Zero) }
    var anchorSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 80.sdp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) {
                onMessageClick(anchorOffset, anchorSize)
            }
            .onGloballyPositioned { layoutCoordinates ->
                anchorOffset = layoutCoordinates.positionInWindow()
                anchorSize = layoutCoordinates.size
            },
        contentAlignment = Alignment.CenterEnd,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 6.sdp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
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

            Row(
                modifier = Modifier
                    .padding(end = 2.sdp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {

                if(message.edited) {
                    Text(
                        text = "Edited",
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.width(5.sdp))
                }

                Text(
                    text = message.sentTimeStamp?.let {
                        getDateFromMillis(
                            message.sentTimeStamp,
                            HH_MM
                        )
                    } ?: "",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.width(5.sdp))

                when {
                    message.seenBy.contains(oppositeUser.id) -> {
                        CheckedStatusIcon()
                    }

                    oppositeUser.onlineStatus.devices.isNotEmpty() -> {
                        ReceivedStatusIcon()
                    }

                    oppositeUser.blockedUsers.contains(mainUser.id) -> {
                        NoneStatusIcon()
                    }

                    else -> {
                        NoneStatusIcon()
                    }
                }
            }
        }
    }
}

@Composable
fun MyImageMessageCard(
    modifier: Modifier = Modifier,
    message: Message,
    oppositeUser: User,
    onMessageClick: (anchorOffset: Offset, anchorSize: IntSize) -> Unit
) {
    val mainUser = LocalUser.current

    val interactionSource = remember { MutableInteractionSource() }

    var anchorOffset by remember { mutableStateOf(Offset.Zero) }
    var anchorSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 60.sdp)
            .heightIn(150.sdp, 320.sdp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) {
                onMessageClick(anchorOffset, anchorSize)
            }
            .onGloballyPositioned { layoutCoordinates ->
                anchorOffset = layoutCoordinates.positionInWindow()
                anchorSize = layoutCoordinates.size
            },
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(horizontal = 10.sdp)
                .align(Alignment.CenterEnd)
                .clip(RoundedCornerShape(10.sdp))
                .border(
                    border = BorderStroke(1.sdp, MaterialTheme.colorScheme.secondary),
                    shape = RoundedCornerShape(10.sdp)
                ),
            model = message.content,
            contentScale = ContentScale.Fit,
            contentDescription = "image"
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.sdp)
                .clip(RoundedCornerShape(12.sdp))
                .background(Color.Black.copy(0.4f))
                .padding(6.sdp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = message.sentTimeStamp?.let {
                    getDateFromMillis(
                        message.sentTimeStamp,
                        HH_MM
                    )
                } ?: "",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.width(5.sdp))

            when {
                message.seenBy.contains(oppositeUser.id) -> {
                    CheckedStatusIcon()
                }

                oppositeUser.onlineStatus.devices.isNotEmpty() -> {
                    ReceivedStatusIcon()
                }

                oppositeUser.blockedUsers.contains(mainUser.id) -> {
                    NoneStatusIcon()
                }

                else -> {
                    NoneStatusIcon()
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
    onMessageClick: (anchorOffset: Offset, anchorSize: IntSize) -> Unit
) {
    var anchorOffset by remember { mutableStateOf(Offset.Zero) }
    var anchorSize by remember { mutableStateOf(IntSize.Zero) }

    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .padding(end = 80.sdp)
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) {
                onMessageClick(anchorOffset, anchorSize)
            }
            .onGloballyPositioned { layoutCoordinates ->
                anchorOffset = layoutCoordinates.positionInWindow()
                anchorSize = layoutCoordinates.size
            },
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
                .background(
                    color = MaterialTheme.colorScheme.surface,
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

                if(message.edited) {
                    Spacer(modifier = Modifier.width(5.sdp))

                    Text(
                        text = "Edited",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun OppositeUserImageMessageCard(
    modifier: Modifier = Modifier,
    message: Message,
    onMessageClick: (anchorOffset: Offset, anchorSize: IntSize) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    var anchorOffset by remember { mutableStateOf(Offset.Zero) }
    var anchorSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(end = 60.sdp)
            .heightIn(150.sdp, 320.sdp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) {
                onMessageClick(anchorOffset, anchorSize)
            }
            .onGloballyPositioned { layoutCoordinates ->
                anchorOffset = layoutCoordinates.positionInWindow()
                anchorSize = layoutCoordinates.size
            },
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 10.sdp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(10.sdp))
                    .border(
                        border = BorderStroke(1.sdp, MaterialTheme.colorScheme.secondary),
                        shape = RoundedCornerShape(10.sdp)
                    ),
                model = message.content,
                contentScale = ContentScale.Fit,
                contentDescription = "image"
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.sdp)
                    .clip(RoundedCornerShape(12.sdp))
                    .background(Color.Black.copy(0.4f))
                    .padding(6.sdp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = message.sentTimeStamp?.let {
                        getDateFromMillis(
                            message.sentTimeStamp,
                            HH_MM
                        )
                    } ?: "",
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
                user = User(name = "Tester"),
            ),
            sendMessageText = "",
            paginatedMessages = flowOf(PagingData.from(fakeMessages)).collectAsLazyPagingItems(),
            navController = rememberNavController(),
            dispatchEvent = { Job().job }
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