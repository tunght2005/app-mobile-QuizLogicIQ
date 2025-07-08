package com.example.logiciq.data.repository

import com.example.logiciq.data.model.ClassItem
import com.example.logiciq.data.model.Member
import com.example.logiciq.data.model.Question
import com.example.logiciq.data.model.Quiz
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID

class ClassRepository {
    private val db = FirebaseFirestore.getInstance()
    private val col = db.collection("classes")

    // ✅ Tạo lớp học và tự động thêm quiz từ thư viện nếu có
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
            members = listOf(member), // ✅ truyền đúng List<Member>
            createdAt = Timestamp.now()
        )

        // Tạo lớp học
        col.document(classId).set(classItem).await()

        // Lấy tất cả bài thi cá nhân để thêm vào lớp
        val tests = db.collection("tests")
            .whereEqualTo("createBy", user.uid)
            .get()
            .await()

        val batch = db.batch()

        tests.documents.forEach { doc ->
            val quizRef = col.document(classId)
                .collection("quizzes")
                .document(doc.id)

            batch.set(quizRef, doc.data ?: return@forEach)
        }

        batch.commit().await()

        return classId
    }


    // ✅ Lấy thông tin lớp học
    suspend fun getClassById(classId: String): ClassItem {
        val doc = col.document(classId).get().await()
        return doc.toObject(ClassItem::class.java)!!.copy(id = doc.id)
    }

    // ✅ Lấy danh sách bài thi trong lớp học (classes/{classId}/quizzes)
    suspend fun getQuizzesByClassId(classId: String): List<Quiz> {
        return try {
            val snapshot = col.document(classId)
                .collection("quizzes")
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                try {
                    val title = doc.getString("title") ?: return@mapNotNull null
                    val questionsData = doc.get("questions") as? List<Map<String, Any>> ?: return@mapNotNull null

                    val questions = questionsData.mapNotNull { q ->
                        val question = q["question"] as? String ?: return@mapNotNull null
                        val answers = q["answers"] as? List<String> ?: return@mapNotNull null
                        val correct = q["correctOption"]?.toString()?.firstOrNull() ?: 'A'

                        if (answers.size != 4) return@mapNotNull null

                        Question.Type4(
                            text = question,
                            optionA = answers[0],
                            optionB = answers[1],
                            optionC = answers[2],
                            optionD = answers[3],
                            correctOption = correct
                        )
                    }

                    Quiz(
                        id = doc.getString("id") ?: doc.id,
                        title = title,
                        questions = questions,
                        maxDurationSeconds = (doc.getLong("maxDurationSeconds") ?: 0).toInt(),
                        createBy = doc.getString("createBy") ?: "",
                        createByName = doc.getString("createByName") ?: "",
                        createdAt = doc.getTimestamp("createdAt") ?: Timestamp.now(),
                        classId = classId
                    )
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
