package com.example.chatapp.model.db.userDbUsecases.posts.userOnlineStatus

import android.content.Context
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.Dtos.user.UserOnlineStatus
import com.example.chatapp.USERS_DB_COLLECTION
import com.example.chatapp.helpers.context.deviceId
import com.example.chatapp.helpers.time.getCurrentTimeInMillis
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DeleteUserDeviceUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firestore: FirebaseFirestore,
) {

    private val deviceId = context.deviceId()
    private val usersCollection = firestore.collection(USERS_DB_COLLECTION)

    operator fun invoke(userId: String) {
        try {
            firestore.runTransaction { transaction ->
                val userRef = usersCollection.document(userId)
                val user = transaction[userRef].toObject<User>()

                user?.let {
                    val devices = user.onlineStatus.devices

                    if(devices.contains(deviceId)) {
                        devices.remove(deviceId)

                        if(devices.isEmpty()) {
                            transaction.update(userRef,"onlineStatus",UserOnlineStatus(
                                lastTimeSeen = getCurrentTimeInMillis(),
                                devices = devices,
                            ))
                        } else {
                            transaction.update(userRef,"onlineStatus",user.onlineStatus)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}