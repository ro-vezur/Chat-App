package com.example.chatapp.model.db.userDbUsecases.posts.friendRequest

import com.example.chatapp.Dtos.notification.NotificationBody
import com.example.chatapp.Dtos.notification.NotificationData
import com.example.chatapp.Dtos.notification.SendNotificationDto
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.example.chatapp.model.db.userDbUsecases.gets.GetUserUseCase
import com.example.chatapp.model.services.messanging.SendRemoteNotificationUseCase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AcceptFriendRequestUseCase @Inject constructor(
    private val db: FirebaseFirestore,
    private val sendRemoteNotificationUseCase: SendRemoteNotificationUseCase,
    private val getUserUseCase: GetUserUseCase
) {
    val usersDb = db.collection(USERS_DB_COLLECTION)
    private var sendNotificationFlag = false

    suspend operator fun invoke(acceptorUserId: String, newFriendId: String) {

        suspend fun addFriend(receiverId: String, senderId: String) {
            db.runTransaction { transaction ->
                val userDocumentRef = usersDb.document(receiverId)
                val user = transaction.get(userDocumentRef).toObject(User::class.java)

                sendNotificationFlag = when {
                    user == null -> false
                    user.friends.contains(senderId) && !user.requests.map { it.userId }.contains(senderId) -> true
                    else -> {
                        val friendsList = user.friends
                        val requests = user.requests

                        friendsList.add(senderId)
                        transaction.update(userDocumentRef,"friends",friendsList)

                        requests.removeAll { it.userId == senderId }
                        transaction.update(userDocumentRef,"requests",requests)

                        true
                    }
                }
            }.await()

            db.runTransaction { transaction ->
                val receiverDocumentRef = usersDb.document(senderId)
                val user = transaction[receiverDocumentRef].toObject(User::class.java)

                sendNotificationFlag = when {
                    user == null -> false
                    !user.sentRequestsToUsers.contains(receiverId) -> true
                    else -> {
                        val sendRequestsToUsers = user.sentRequestsToUsers
                        sendRequestsToUsers.remove(receiverId)
                        transaction.update(receiverDocumentRef,"sentRequestsToUsers",sendRequestsToUsers)
                        true
                    }
                }
            }.await()
        }

        addFriend(acceptorUserId,newFriendId)
        addFriend(newFriendId,acceptorUserId)

        val sender = getUserUseCase(acceptorUserId)
        val receiver = getUserUseCase(newFriendId)

        if(sendNotificationFlag && sender != null && receiver != null){
            receiver.fcmTokens.forEach { token ->
                sendRemoteNotificationUseCase(
                    sendNotificationDto = SendNotificationDto(
                        token = token,
                        topic = null,
                        notificationBody = NotificationBody(
                            title = "New Friend!",
                            body = "${sender.name} Accepted Your Friend Request!"
                        ),
                        data = NotificationData(
                            senderId = sender.id,
                            receiverId = receiver.id,
                            type = "accept_friend_request"
                        )
                    ),
                )
            }
        }
    }
}