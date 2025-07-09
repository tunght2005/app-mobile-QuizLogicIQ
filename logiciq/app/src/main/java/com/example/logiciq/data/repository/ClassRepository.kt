package com.example.logiciq.data.repository

import com.example.logiciq.data.mapper.toQuizOrNull
import com.example.logiciq.data.model.ClassItem
import com.example.logiciq.data.model.Member
import com.example.logiciq.data.model.Quiz
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID

class ClassRepository {
    private val db = FirebaseFirestore.getInstance()
    private val col = db.collection("classes")
    private val quizCol = db.collection("quizzes")

    /**
     * ✅ Tạo lớp học mới, thêm người tạo làm thành viên
     */
    suspend fun createClass(name: String, description: String): String {
        val user = FirebaseAuth.getInstance().currentUser
            ?: throw Exception("Chưa đăng nhập")

        val classId = UUID.randomUUID().toString()

        val member = Member(
            userId = user.uid,
            userName = user.displayName ?: user.email ?: "Không tên",
            avatar = user.photoUrl?.toString()
        )

        val classItem = ClassItem(
            id = classId,
            name = name,
            description = description,
            createdBy = user.uid,
            creatorName = user.displayName ?: "Không tên",
            members = listOf(member),
            createdAt = Timestamp.now()
        )

        col.document(classId).set(classItem).await()
        return classId
    }
    suspend fun shareQuizToClass(quizId: String, classId: String) {
        val quizRef = quizCol.document(quizId)
        val snapshot = quizRef.get().await()
        val existingIds = snapshot.get("classIds") as? List<String> ?: emptyList()
        val updatedIds = existingIds.toMutableSet().apply { add(classId) }.toList()
        quizRef.update("classIds", updatedIds).await()
    }


    /**
     * ✅ Lấy thông tin chi tiết của một lớp học
     */
    suspend fun getClassById(classId: String): ClassItem {
        val doc = col.document(classId).get().await()
        return doc.toObject(ClassItem::class.java)!!.copy(id = doc.id)
    }

    /**
     * ✅ Lấy danh sách bài thi thuộc lớp này (dựa vào classIds chứa classId)
     */
    suspend fun getQuizzesForClass(classId: String): List<Quiz> {
        return try {
            val snapshot = quizCol
                .whereArrayContains("classIds", classId)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toQuizOrNull() // ⬅ dùng hàm custom chuyển từ DocumentSnapshot sang Quiz
            }
        } catch (e: Exception) {
            emptyList()
        }
    }



    /**
     * ✅ Lấy danh sách ClassItem đầy đủ của user
     */
    suspend fun getClassesForUser(userId: String): List<ClassItem> {
        return try {
            col.get().await()
                .mapNotNull { doc ->
                    val classItem = doc.toObject(ClassItem::class.java)
                    if (classItem.members.any { it.userId == userId }) {
                        classItem.copy(id = doc.id)
                    } else null
                }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
