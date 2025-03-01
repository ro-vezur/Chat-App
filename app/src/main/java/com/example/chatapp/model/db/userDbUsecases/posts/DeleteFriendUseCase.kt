package com.example.chatapp.model.db.userDbUsecases.posts

import com.example.chatapp.Dtos.user.User
import com.example.chatapp.Dtos.notification.NotificationBody
import com.example.chatapp.Dtos.notification.NotificationData
import com.example.chatapp.Dtos.notification.SendNotificationDto
import com.example.chatapp.USERS_DB_COLLECTION
import com.example.chatapp.model.db.userDbUsecases.gets.GetCurrentUserIdUseCase
import com.example.chatapp.model.db.userDbUsecases.gets.GetUserUseCase
import com.example.chatapp.model.services.messanging.SendRemoteNotificationUseCase
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DeleteFriendUseCase @Inject constructor(
    private val db: FirebaseFirestore,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val sendRemoteNotificationUseCase: SendRemoteNotificationUseCase,
    private val getUserUseCase: GetUserUseCase
) {
    private val usersDb = db.collection(USERS_DB_COLLECTION)
    private var sendNotificationFlag = false

    suspend operator fun invoke(friendId: String,onSuccess: () -> Unit,) {
        fun deleteFriend(userId: String,friendId: String): Task<Unit> {
            return db.runTransaction { transaction ->
                val userDocumentRef = usersDb.document(userId)
                val user = transaction[userDocumentRef].toObject(User::class.java)

                if(user != null) {
                    val friends = user.friends
                    if(friends.contains(friendId)) {
                       friends.remove(friendId)
                       sendNotificationFlag = true
                       transaction.update(userDocumentRef,"friends",friends)
                    } else {
                        sendNotificationFlag = false
                    }
                }
            }
        }

        deleteFriend(getCurrentUserIdUseCase(),friendId).await()
        deleteFriend(friendId,getCurrentUserIdUseCase()).await()

        val sender = getUserUseCase(getCurrentUserIdUseCase())
        val receiver = getUserUseCase(friendId)

        if(sendNotificationFlag && sender != null && receiver != null) {
           receiver.fcmTokens.forEach { token ->
               sendRemoteNotificationUseCase(
                   sendNotificationDto = SendNotificationDto(
                       token = token,
                       topic = null,
                       notificationBody = NotificationBody(
                           title = "Lost a Friend!",
                           body = "${sender.name} Deleted You From Friends List!"
                       ),
                       data = NotificationData(
                           senderId = sender.id,
                           receiverId = receiver.id,
                           type = "delete_friend"
                       )
                   )
               )
           }

            onSuccess()
        }
    }
}