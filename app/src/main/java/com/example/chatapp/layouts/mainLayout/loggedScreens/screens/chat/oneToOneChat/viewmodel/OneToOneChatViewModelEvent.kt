package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.oneToOneChat.viewmodel

import android.net.Uri
import com.example.chatapp.Dtos.chat.LocalChatInfo
import com.example.chatapp.Dtos.chat.Message

sealed class OneToOneChatViewModelEvent {
    class AddLocalChatInfo(val userId: String,val localChatInfo: LocalChatInfo): OneToOneChatViewModelEvent()
    class SendMessage(val message: Message): OneToOneChatViewModelEvent()
    class ChangeSendMessageText(val query: String): OneToOneChatViewModelEvent()
    class AddMessageToReadList(val message: Message, val userId: String): OneToOneChatViewModelEvent()
    data object ClearMessagesReadList: OneToOneChatViewModelEvent()
    class SetMessagesReadStatus(val userId: String): OneToOneChatViewModelEvent()
    class SetUnseenMessagesCount(val userId: String): OneToOneChatViewModelEvent()
    class AddUserTyping(val chatId: String,val userId: String): OneToOneChatViewModelEvent()
    class RemoveUserTyping(val chatId: String,val userId: String): OneToOneChatViewModelEvent()
    class DeleteMessage(val messageId: String,val chatId: String): OneToOneChatViewModelEvent()
    class ConfirmMessageChanges(val newMessage: Message): OneToOneChatViewModelEvent()
    class ChangeEditModeState(val message: Message?): OneToOneChatViewModelEvent()
    class AddImagesToSend(val uris: List<Uri>): OneToOneChatViewModelEvent()
    data object ClearSelectedImages : OneToOneChatViewModelEvent()
    class RemoveMediaFromSelection(val media: Uri): OneToOneChatViewModelEvent()
    data object SendMedia: OneToOneChatViewModelEvent()
}