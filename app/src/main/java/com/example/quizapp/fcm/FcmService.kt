package com.example.logiciq.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.logiciq.R
import com.example.logiciq.ui.main.MainActivity
import com.example.logiciq.ui.quiz.QuizDetailActivity
import com.example.logiciq.ui.schedule.FakeScheduleActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Gửi token lên Firestore nếu cần
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val data = remoteMessage.data
        val title = data["title"] ?: remoteMessage.notification?.title ?: "QuizApp"
        val body = data["body"] ?: remoteMessage.notification?.body ?: ""
        val type = data["type"] ?: "general"

        showNotification(title, body, type, data)
    }

    private fun showNotification(
        title: String,
        message: String,
        type: String,
        data: Map<String, String>
    ) {
        val channelId = "quiz_app_channel"
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "QuizApp Notifications", NotificationManager.IMPORTANCE_HIGH
            )
            nm.createNotificationChannel(channel)
        }

        val intent = when (type) {
            "quiz" -> Intent(this, QuizDetailActivity::class.java).apply {
                putExtra("quizId", data["quizId"])
            }
            "schedule" -> Intent(this, FakeScheduleActivity::class.java).apply {
                putExtra("scheduleId", data["scheduleId"])
            }
            else -> Intent(this, MainActivity::class.java)
        }.apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        nm.notify(System.currentTimeMillis().toInt(), notification)
    }
}
