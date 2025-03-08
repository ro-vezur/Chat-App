package com.example.chatapp.model.db.userDbUsecases.posts.friendRequest

import com.example.chatapp.Dtos.notification.NotificationBody
import com.example.chatapp.Dtos.notification.NotificationData
import com.example.chatapp.Dtos.notification.SendNotificationDto
import com.example.chatapp.Dtos.requests.FriendRequest
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.example.chatapp.helpers.time.getCurrentTimeInMillis
import com.example.chatapp.model.services.messanging.SendRemoteNotificationUseCase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SendFriendRequestUseCase @Inject constructor(
    private val db: FirebaseFirestore,
    private val sendRemoteNotificationUseCase: SendRemoteNotificationUseCase,
) {
    private val usersDb = db.collection(USERS_DB_COLLECTION)
    private var sendNotificationFlag = false

    suspend operator fun invoke(sender: User, receiver: User){
        db.runTransaction { transaction ->
            val senderDocumentRef = usersDb.document(sender.id)
            val user = transaction.get(senderDocumentRef).toObject(User::class.java)

            sendNotificationFlag = when {
                user == null -> false
                user.sentRequestsToUsers.contains(receiver.id) || user.friends.contains(receiver.id) -> false
                else -> {
                    val sentRequestsToUsers = user.sentRequestsToUsers
                    sentRequestsToUsers.add(receiver.id)
                    transaction.update(senderDocumentRef, "sentRequestsToUsers", sentRequestsToUsers)
                    true
                }
            }
        }.await()

        db.runTransaction { transaction ->
            val receiverDocumentRef = usersDb.document(receiver.id)
            val user = transaction.get(receiverDocumentRef).toObject(User::class.java)

            sendNotificationFlag = when {
                user == null -> false
                user.requests.map { it.userId }.contains(sender.id) || user.friends.contains(sender.id) -> false
                else -> {
                    val requests = user.requests
                    requests.add(FriendRequest(sender.id, getCurrentTimeInMillis()))
                    transaction.update(receiverDocumentRef, "requests", requests)
                    true
                }
            }
        }.await()

        if(sendNotificationFlag) {
            receiver.fcmTokens.forEach { token ->
                sendRemoteNotificationUseCase(
                    sendNotificationDto = SendNotificationDto(
                        token = token.key,
                        topic = null,
                        notificationBody = NotificationBody(
                            title = "Friend Request",
                            body = "${sender.name} Sent You Friend Request!"
                        ),
                        data = NotificationData(
                            senderId = sender.id,
                            receiverId = receiver.id,
                            type = "friend_request"
                        )
                    )
                )
            }
        }
    }
}