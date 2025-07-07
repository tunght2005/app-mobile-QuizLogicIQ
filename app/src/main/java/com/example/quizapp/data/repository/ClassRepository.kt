package com.example.logiciq.data.repository

import com.example.logiciq.data.model.ClassItem
import com.example.logiciq.data.model.SubjectItem
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ClassRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    private val col = db.collection("classes")

    suspend fun createClass(name: String, description: String): Result<String> {
        val user = auth.currentUser ?: return Result.failure(Exception("Chưa đăng nhập"))
        val newDoc = col.document()
        val item = ClassItem(
            id = newDoc.id,
            name = name,
            description = description,
            createdBy = user.uid,
            creatorName = user.displayName ?: user.email ?: "Unknown",
            createdAt = Timestamp.now()
        )
        return try {
            newDoc.set(item).await()
            Result.success(newDoc.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class SubjectRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    private val col = db.collection("subjects")

    suspend fun createSubject(title: String, term: String, definition: String): Result<String> {
        val user = auth.currentUser ?: return Result.failure(Exception("Chưa đăng nhập"))
        val newDoc = col.document()
        val item = SubjectItem(
            id = newDoc.id,
            title = title,
            term = term,
            definition = definition,
            createdBy = user.uid,
            creatorName = user.displayName ?: user.email ?: "Unknown",
            createdAt = Timestamp.now()
        )
        return try {
            newDoc.set(item).await()
            Result.success(newDoc.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
