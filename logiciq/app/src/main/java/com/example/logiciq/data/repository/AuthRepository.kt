package com.example.logiciq.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.logiciq.data.model.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun updateUserProfile(name: String, email: String): Result<Unit> {
        val firebaseUser = auth.currentUser ?: return Result.failure(Exception("Người dùng chưa đăng nhập"))
        return try {
            val profileUpdate = userProfileChangeRequest {
                displayName = name
            }
            firebaseUser.updateProfile(profileUpdate).await()
            firebaseUser.updateEmail(email).await()

            val updates = mapOf(
                "name" to name,
                "email" to email
            )
            firestore.collection("users").document(firebaseUser.uid).update(updates).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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
            Result.failure(e)
        }
    }

    suspend fun loginWithEmail(email: String, password: String): Result<User> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = auth.currentUser ?: return Result.failure(Exception("Không tìm thấy user"))

            val userDoc = firestore.collection("users").document(firebaseUser.uid)
            val snapshot = userDoc.get().await()

            if (!snapshot.exists()) {
                val user = User(
                    uid = firebaseUser.uid,
                    name = firebaseUser.displayName ?: "",
                    email = firebaseUser.email ?: "",
                    photoUrl = firebaseUser.photoUrl?.toString(),
                    createdAt = Timestamp.now(),
                    lastLogin = Timestamp.now()
                )
                userDoc.set(user).await()
            } else {
                userDoc.update("lastLogin", FieldValue.serverTimestamp()).await()
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

            val profile = userProfileChangeRequest {
                displayName = name
            }
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

    suspend fun uploadAvatar(uri: Uri, context: Context): Result<String> {
        val user = auth.currentUser ?: return Result.failure(Exception("Chưa đăng nhập"))

        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes() ?: return Result.failure(Exception("Không đọc được ảnh"))
            val base64Image = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)

            val apiKey = "a288112f95016a6959c9b795e374cf13"
            val url = URL("https://api.imgbb.com/1/upload?key=$apiKey")

            val body = "image=$base64Image"
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            connection.outputStream.use { it.write(body.toByteArray()) }

            val response = connection.inputStream.bufferedReader().readText()
            val json = org.json.JSONObject(response)
            val imageUrl = json.getJSONObject("data").getString("url")

            // Cập nhật Firebase Auth
            val profileUpdates = userProfileChangeRequest {
                photoUri = Uri.parse(imageUrl)
            }
            user.updateProfile(profileUpdates).await()

            // Cập nhật Firestore
            firestore.collection("users").document(user.uid)
                .update("photoUrl", imageUrl).await()

            println(" Upload thành công: $imageUrl")
            Result.success(imageUrl)

        } catch (e: Exception) {
            println("Upload error: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }





    fun signOut() {
        auth.signOut()
    }

    fun getCurrentFirebaseUser() = auth.currentUser
}
