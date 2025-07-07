package com.example.logiciq.fcm

import android.app.Application
import com.google.firebase.FirebaseApp

class FirebaseInitializer : Application() {
    override fun onCreate() {
        super.onCreate()

        // Khởi tạo Firebase nếu chưa khởi tạo
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }
    }
}
