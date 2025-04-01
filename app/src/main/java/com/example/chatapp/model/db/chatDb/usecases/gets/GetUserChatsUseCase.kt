package com.example.chatapp.model.db.chatDb.usecases.gets

import com.example.chatapp.CHATS_DB
import com.example.chatapp.Dtos.chat.Chat
import com.example.chatapp.Dtos.chat.ChatUI
import com.example.chatapp.Dtos.chat.LocalChatInfo
import com.example.chatapp.Dtos.chat.chatType.ChatType
import com.example.chatapp.model.db.messagesDbUseCases.gets.GetChatMessageUseCase
import com.example.chatapp.model.db.messagesDbUseCases.gets.GetUnseenMessagesCountUseCase
import com.example.chatapp.model.db.userDbUsecases.observers.ObserveUserUseCase
import com.example.chatapp.others.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetUserChatsUseCase @Inject constructor(
    db: FirebaseFirestore,
    private val observeUserUseCase: ObserveUserUseCase,
    private val getChatMessageUseCase: GetChatMessageUseCase,
    private val getUnseenMessagesCountUseCase: GetUnseenMessagesCountUseCase,
) {
    private val chatsDb = db.collection(CHATS_DB)

    operator fun invoke(userId: String, contacts: List<LocalChatInfo> ): Flow<Resource<List<ChatUI>>> = callbackFlow<Resource<List<ChatUI>>> {
        val listener = chatsDb
            .whereArrayContains("users",userId)
            .addSnapshotListener { snapshot, error ->
                if(error != null) {
                    close(error.cause)
                    return@addSnapshotListener
                }

                snapshot?.let {
                    val chats = snapshot.documents.mapNotNull { it.toObject(Chat::class.java) }
                    val chatsUI = mutableListOf<ChatUI>()

                    CoroutineScope(Dispatchers.IO).launch {
                        chats.forEach { chat ->
                            val contact = contacts.find { contact -> contact.id == chat.id }

                            contact?.let {
                                val lastReadMessage = getChatMessageUseCase(chat.id,chat.lastReads[userId] ?: "")
                                val isPinned = contact.isPinned
                                when(chat.chatType) {
                                    ChatType.USER -> {
                                        val oppositeUserId = chat.getOppositeUserId(userId)

                                        oppositeUserId?.let {
                                            observeUserUseCase(oppositeUserId) .collectLatest { oppositeUser ->
                                                val chatUI = ChatUI(
                                                    id = chat.id,
                                                    chatType = chat.chatType,
                                                    usersTyping = chat.usersTyping,
                                                    lastMessage = getChatMessageUseCase(chat.id,chat.lastMessageId),
                                                    isPinned = isPinned,
                                                    name = oppositeUser.name,
                                                    imageUrl = oppositeUser.imageUrl,
                                                    userId = chat.getOppositeUserId(userId),
                                                    unseenMessagesCount = getUnseenMessagesCountUseCase(chat.id,lastReadMessage)
                                                )

                                                if(chatsUI.map { it.id }.contains(chatUI.id)) {
                                                    val chatUiIndex = chatsUI.map { it.id }.indexOf(chatUI.id)
                                                    if(chatUiIndex != -1) {
                                                        chatsUI[chatUiIndex] = chatUI
                                                    }
                                                } else {
                                                    chatsUI.add(chatUI)
                                                }

                                                trySend(Resource.Success(data = chatsUI.toList()))
                                            }
                                        }
                                    }
                                    ChatType.GROUP -> {
                                        val chatUI = ChatUI(
                                            id = chat.id,
                                            chatType = chat.chatType,
                                            usersTyping = chat.usersTyping,
                                            lastMessage = getChatMessageUseCase(chat.id,chat.lastMessageId),
                                            isPinned = isPinned,
                                            name = chat.name,
                                            imageUrl = chat.imageUrl,
                                            userId = null
                                        )

                                        chatsUI.add(chatUI)
                                        trySend(Resource.Success(data = chatsUI.toList()))
                                    }
                                }
                            }

                        }
                    }
                }
            }

        awaitClose { listener.remove() }
    }.catch { e ->
        emit(Resource.Error(message = e.message.toString()))
        e.printStackTrace()
    }
}