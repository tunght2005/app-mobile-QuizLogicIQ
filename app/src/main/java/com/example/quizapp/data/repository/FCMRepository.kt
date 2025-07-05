package com.example.logiciq.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FCMRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun saveToken(token: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userRef = firestore.collection("users").document(uid)
        userRef.update("fcmToken", token)
    }
}
