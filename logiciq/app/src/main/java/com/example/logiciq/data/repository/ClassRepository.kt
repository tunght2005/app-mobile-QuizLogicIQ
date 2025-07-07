package com.example.logiciq.data.repository

import com.example.logiciq.data.model.ClassItem
import com.example.logiciq.data.model.Member
import com.example.logiciq.data.model.Quiz
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ClassRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    private val col = db.collection("classes")

    suspend fun createClass(
        name: String,
        description: String,
        quiz: Quiz
    ): Result<String> {
        val user = auth.currentUser ?: return Result.failure(Exception("Chưa đăng nhập"))

        val member = Member(
            userId = user.uid,
            userName = user.displayName ?: user.email ?: "Unknown",
            avatar = user.photoUrl?.toString()
        )

        val newDoc = col.document()

        val classItem = ClassItem(
            id = newDoc.id,
            name = name,
            memberCount = 1,
            members = listOf(member),
            quiz = quiz.copy(id = "quiz_${newDoc.id}"),
            description = description,
            createdBy = user.uid,
            creatorName = member.userName,
            createdAt = quiz.createdAt
        )

        return try {
            newDoc.set(classItem).await()
            Result.success(newDoc.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ✅ Hàm cần thêm để fix lỗi
    suspend fun getClassById(classId: String): ClassItem? {
        return try {
            val doc = db.collection("classes").document(classId).get().await()
            doc.toObject(ClassItem::class.java)?.copy(id = doc.id)
        } catch (e: Exception) {
            null
        }
    }
}
