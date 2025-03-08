package com.example.chatapp.model.services.messanging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.chatapp.Dtos.notification.NotificationBody
import com.example.chatapp.MainActivity
import com.example.chatapp.R
import com.example.chatapp.helpers.time.getCurrentTimeInMillis
import com.example.chatapp.model.db.userDbUsecases.posts.fcmTokenUsecases.AddFcmTokenUseCase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseNotificationService: FirebaseMessagingService() {

    @Inject lateinit var updateCurrentUserTokenUseCase: AddFcmTokenUseCase

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        updateCurrentUserTokenUseCase(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        sendLocalNotification(this.applicationContext,
            notificationData = NotificationBody(
                title = message.notification?.title.toString(),
                body = message.notification?.body.toString(),
            )
        )
    }
}

private fun sendLocalNotification(context: Context, notificationData: NotificationBody) {
    val intent = Intent(context, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
    )
    val notificationChannelStr = "DEFAULT_CHANNEL"
    val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    val notification = NotificationCompat.Builder(context,notificationChannelStr)
        .setSmallIcon(R.drawable.ic_notification_active)
        .setContentTitle(notificationData.title)
        .setContentText(notificationData.body)
        .setAutoCancel(true)
        .setSound(defaultSoundUri)
        .setContentIntent(pendingIntent)
        .build()

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val channel = NotificationChannel(
            notificationChannelStr,
            "Messages",
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationManager.createNotificationChannel(channel)
    }

    val notificationId = getCurrentTimeInMillis().toInt()
    notificationManager.notify(notificationId,notification)
}