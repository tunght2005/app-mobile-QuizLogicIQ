package com.example.logiciq.data.repository

import com.example.logiciq.data.model.Question
import com.example.logiciq.data.model.Quiz
import com.example.logiciq.data.model.QuizResult
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class QuizRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val quizCollection = db.collection("quizzes")
    private val resultCollection = db.collection("quiz_results")

    // Thêm Quiz mới vào Firestore
    fun saveQuiz(quiz: Quiz, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        val data = mapOf(
            "id" to quiz.id,
            "title" to quiz.title,
            "maxDurationSeconds" to quiz.maxDurationSeconds,
            "createdAt" to quiz.createdAt,
            "createBy" to quiz.createBy,
            "questions" to quiz.questions.map { q ->
                when (q) {
                    is Question.Type2 -> mapOf(
                        "id" to q.id,
                        "text" to q.text,
                        "type" to "TYPE2",
                        "correctAnswer" to q.correctAnswer,
                        "wrongPool" to q.wrongPool
                    )
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

    // Lưu kết quả làm bài của người dùng
    fun saveQuizResult(
        result: QuizResult,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        resultCollection.add(result)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e) }
    }

    // Lấy tất cả Quiz (ví dụ cho giao diện danh sách bài quiz)
    fun getAllQuizzes(
        onSuccess: (List<Quiz>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        quizCollection.get()
            .addOnSuccessListener { snapshot ->
                val quizzes = snapshot.documents.mapNotNull { doc ->
                    doc.toQuizOrNull()
                }
                onSuccess(quizzes)
            }
            .addOnFailureListener { e -> onError(e) }
    }

    // Lấy một Quiz theo ID
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

    // Extension function hỗ trợ mapping document thành Quiz
    private fun Map<String, Any>.toQuestion(): Question? {
        val type = this["type"] as? String ?: return null
        val id = this["id"] as? String ?: return null
        val text = this["text"] as? String ?: return null

        return when (type) {
            "TYPE2" -> {
                val correctAnswer = this["correctAnswer"] as? String ?: return null
                val wrongPool = this["wrongPool"] as? List<String> ?: return null
                Question.Type2(id = id, text = text, correctAnswer = correctAnswer, wrongPool = wrongPool)
            }
            "TYPE4" -> {
                val options = this["options"] as? Map<String, String> ?: return null
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

    private fun com.google.firebase.firestore.DocumentSnapshot.toQuizOrNull(): Quiz? {
        val id = getString("id") ?: return null
        val title = getString("title") ?: return null
        val maxDurationSeconds = getLong("maxDurationSeconds")?.toInt() ?: return null
        val createBy = getString("createBy") ?: ""
        val createdAt = getTimestamp("createdAt") ?: Timestamp.now()
        val questionsData = get("questions") as? List<Map<String, Any>> ?: return null

        val questions = questionsData.mapNotNull { it.toQuestion() }

        return Quiz(
            id = id,
            title = title,
            questions = questions,
            maxDurationSeconds = maxDurationSeconds,
            createBy = createBy,
            createdAt = createdAt
        )
    }
}
