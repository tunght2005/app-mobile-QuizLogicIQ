package com.example.logiciq.data.repository

import android.util.Log
import com.example.logiciq.data.model.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun firebaseAuthWithGoogle(idToken: String): Result<User> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            val firebaseUser = authResult.user ?: return Result.failure(Exception("No user returned"))

            val user = User(
                uid = firebaseUser.uid,
                name = firebaseUser.displayName ?: "",
                email = firebaseUser.email ?: "",
                photoUrl = firebaseUser.photoUrl?.toString(),
                createdAt = Timestamp.now(),
                lastLogin = Timestamp.now()
            )

            val userDoc = firestore.collection("users").document(user.uid)
            val snapshot = userDoc.get().await()

            if (!snapshot.exists()) {
                userDoc.set(user).await()
            } else {
                userDoc.update("lastLogin", FieldValue.serverTimestamp()).await()
            }

            Result.success(user)
        } catch (e: Exception) {
            Log.e("AuthRepo", "Google Sign-In failed", e)
            Result.failure(e)
        }
    }

    suspend fun loginWithEmail(email: String, password: String): Result<User> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = auth.currentUser ?: return Result.failure(Exception("Không tìm thấy user"))

            val userDoc = firestore.collection("users").document(firebaseUser.uid)
            val snapshot = userDoc.get().await()

            if (snapshot.exists()) {
                // Cập nhật lần đăng nhập gần nhất
                userDoc.update("lastLogin", FieldValue.serverTimestamp()).await()
            } else {
                // Nếu chưa có, tạo mới
                val user = User(
                    uid = firebaseUser.uid,
                    name = firebaseUser.displayName ?: "",
                    email = firebaseUser.email ?: "",
                    photoUrl = firebaseUser.photoUrl?.toString(),
                    createdAt = Timestamp.now(),
                    lastLogin = Timestamp.now()
                )
                userDoc.set(user).await()
            }

            val user = userDoc.get().await().toObject(User::class.java)
                ?: return Result.failure(Exception("Không đọc được user từ Firestore"))

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun registerWithEmail(name: String, email: String, password: String, phone: String?): Result<User> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: return Result.failure(Exception("No user returned"))

            val profile = userProfileChangeRequest { displayName = name }
            firebaseUser.updateProfile(profile).await()

            val user = User(
                uid = firebaseUser.uid,
                name = name,
                email = email,
                photoUrl = firebaseUser.photoUrl?.toString(),
                createdAt = Timestamp.now(),
                lastLogin = Timestamp.now()
            )

            firestore.collection("users").document(user.uid).set(user).await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendResetPasswordEmail(email: String): Result<Unit> {
        return try {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}