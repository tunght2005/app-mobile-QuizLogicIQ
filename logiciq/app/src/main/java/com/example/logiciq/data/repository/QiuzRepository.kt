package com.example.logiciq.data.repository

import com.example.logiciq.data.model.Question
import com.example.logiciq.data.model.Quiz
import com.example.logiciq.data.model.QuizResult
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.example.logiciq.data.mapper.toQuizOrNull


class QuizRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val quizCollection = db.collection("quizzes")
    private val resultCollection = db.collection("quiz_results")

    suspend fun getUserClassIds(userId: String): List<String> {
        val snapshot = db.collection("classes")
            .whereArrayContains("members", userId)
            .get()
            .await()
        return snapshot.documents.mapNotNull { it.id }
    }

    // ✅ Thêm Quiz mới vào Firestore
    fun saveQuiz(quiz: Quiz, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        val data = mapOf(
            "id" to quiz.id,
            "title" to quiz.title,
            "maxDurationSeconds" to quiz.maxDurationSeconds,
            "createdAt" to quiz.createdAt,
            "createBy" to quiz.createdBy,
            "createByName" to quiz.createdByName,
            "classIds" to quiz.classIds,
            "questions" to quiz.questions.map { q ->
                when (q) {
                    is Question.Type4 -> mapOf(
                        "id" to q.id,
                        "text" to q.text,
                        "type" to "TYPE4",
                        "options" to mapOf(
                            "A" to q.optionA,
                            "B" to q.optionB,
                            "C" to q.optionC,
                            "D" to q.optionD
                        ),
                        "correctOption" to q.correctOption.toString()
                    )
                }
            }
        )

        quizCollection.document(quiz.id)
            .set(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e) }
    }

    // ✅ Xoá quiz theo ID
    suspend fun deleteQuiz(quizId: String) {
        quizCollection.document(quizId).delete().await()
    }

    // ✅ Lưu kết quả làm bài
    fun saveQuizResult(
        result: QuizResult,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        resultCollection.add(result)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e) }
    }

    // ✅ Lấy tất cả quiz do người dùng tạo
    fun getQuizzesByUserId(
        userId: String,
        onSuccess: (List<Quiz>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        quizCollection
            .whereEqualTo("createBy", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val quizzes = snapshot.documents.mapNotNull { it.toQuizOrNull() }
                onSuccess(quizzes)
            }
            .addOnFailureListener { e -> onError(e) }
    }

    // ✅ Lấy tất cả quiz thuộc một lớp
    fun getQuizzesByClassId(
        classId: String,
        onSuccess: (List<Quiz>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        quizCollection
            .whereArrayContains("classIds", classId)
            .get()
            .addOnSuccessListener { snapshot ->
                val quizzes = snapshot.documents.mapNotNull { it.toQuizOrNull() }
                onSuccess(quizzes)
            }
            .addOnFailureListener { e -> onError(e) }
    }

    // ✅ Lấy quiz theo ID
    fun getQuizById(
        quizId: String,
        onSuccess: (Quiz) -> Unit,
        onError: (Exception) -> Unit
    ) {
        quizCollection.document(quizId).get()
            .addOnSuccessListener { document ->
                val quiz = document.toQuizOrNull()
                if (quiz != null) {
                    onSuccess(quiz)
                } else {
                    onError(Exception("Quiz không tồn tại"))
                }
            }
            .addOnFailureListener { e -> onError(e) }
    }

    // ✅ Chuyển Map thành Question
    // ✅ Chuyển Map thành Question (đã fix lỗi null cast)
    private fun Map<String, Any>.toQuestion(): Question? {
        val type = this["type"] as? String ?: return null
        val id = this["id"] as? String ?: return null
        val text = this["text"] as? String ?: return null

        return when (type) {
            "TYPE4" -> {
                val optionsRaw = this["options"] as? Map<*, *> ?: return null
                val options = optionsRaw.mapNotNull { (key, value) ->
                    if (key is String && value is String) key to value else null
                }.toMap()

                val correctOptionStr = this["correctOption"] as? String ?: return null
                val correctOption = correctOptionStr.firstOrNull() ?: return null

                Question.Type4(
                    id = id,
                    text = text,
                    optionA = options["A"] ?: "",
                    optionB = options["B"] ?: "",
                    optionC = options["C"] ?: "",
                    optionD = options["D"] ?: "",
                    correctOption = correctOption
                )
            }

            else -> null
        }
    }
}



