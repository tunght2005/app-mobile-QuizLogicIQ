package com.example.logiciq.service

import android.util.Log
import com.example.logiciq.data.repository.FCMRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
        FCMRepository.saveToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.notification?.title
        val body = message.notification?.body
        Log.d("FCM", "Message received: $title - $body")
        // TODO: Thêm code hiển thị thông báo nếu muốn
    }
}
