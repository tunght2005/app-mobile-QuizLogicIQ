package com.example.logiciq.data.repository

import com.example.logiciq.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getCurrentUser(): User? {
        val uid = auth.currentUser?.uid ?: return null
        val doc = firestore.collection("users").document(uid).get().await()
        return doc.toObject(User::class.java)
    }

    fun logout() {
        auth.signOut()
    }
}
