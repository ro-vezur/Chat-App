package com.example.chatapp.model.db.userDbUsecases.posts.friendRequest

import com.example.chatapp.Dtos.user.User
import com.example.chatapp.Dtos.notification.NotificationBody
import com.example.chatapp.Dtos.notification.NotificationData
import com.example.chatapp.Dtos.notification.SendNotificationDto
import com.example.chatapp.USERS_DB_COLLECTION
import com.example.chatapp.model.db.userDbUsecases.gets.GetCurrentUserIdUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetUserUseCase
import com.example.chatapp.model.services.messanging.SendRemoteNotificationUseCase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DeclineFriendRequestUseCase @Inject constructor(
    private val db: FirebaseFirestore,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val sendRemoteNotificationUseCase: SendRemoteNotificationUseCase,
    private val getUserUseCase: GetUserUseCase,
) {
    val usersDb = db.collection(USERS_DB_COLLECTION)
    private var sendNotificationFlag = false

    suspend operator fun invoke(requestUserId: String) {

        val currentUserId = getCurrentUserIdUseCase()

        db.runTransaction { transaction ->
            val userDocumentRef = usersDb.document(currentUserId)
            val user = transaction[userDocumentRef].toObject(User::class.java)

            sendNotificationFlag = when {
                user == null -> false
                !user.requests.map { it.userId }.contains(requestUserId) -> true
                else -> {
                    val requests = user.requests
                    requests.removeAll { it.userId == requestUserId }
                    sendNotificationFlag = true
                    transaction.update(userDocumentRef,"requests",requests)
                    true
                }
            }
        }.await()

        db.runTransaction { transaction ->
            val userDocumentRef = usersDb.document(requestUserId)
            val user = transaction[userDocumentRef].toObject(User::class.java)

            sendNotificationFlag = when {
                user == null -> false
                !user.sentRequestsToUsers.contains(currentUserId) -> true
                else -> {
                    val requestsSentToUsers = user.sentRequestsToUsers
                    requestsSentToUsers.remove(currentUserId)
                    transaction.update(userDocumentRef,"sentRequestsToUsers",requestsSentToUsers)
                    true
                }
            }
        }.await()

        val sender = getUserUseCase(getCurrentUserIdUseCase())
        val receiver = getUserUseCase(requestUserId)

        if(sendNotificationFlag && sender != null && receiver != null) {
            receiver.fcmTokens.forEach { token ->
                sendRemoteNotificationUseCase(
                    sendNotificationDto = SendNotificationDto(
                        token = token,
                        topic = null,
                        notificationBody = NotificationBody(
                            title = "Request Decline!",
                            body = "${sender.name} Declined Your Friend Request!"
                        ),
                        data = NotificationData(
                            senderId = sender.id,
                            receiverId = receiver.id,
                            type = "decline_friend_request"
                        )
                    )
                )
            }
        }
    }
}