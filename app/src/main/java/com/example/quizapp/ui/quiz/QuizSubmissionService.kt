package com.example.logiciq.backend

import android.util.Log
import com.example.logiciq.data.model.*
import com.example.logiciq.data.repository.QuizRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Timestamp

object QuizSubmissionService {
    private val quizRepo = QuizRepository()

    fun submitQuizResult(
        quiz: Quiz,
        answers: List<Answer>,
        timeTaken: Long,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            onError(Exception("Người dùng chưa đăng nhập"))
            return
        }

        val score = answers.count { it.isCorrect } * 10

        val result = QuizResult(
            quizId = quiz.id,
            userId = currentUser.uid,
            userEmail = currentUser.email ?: "",
            userName = currentUser.displayName ?: currentUser.email?.substringBefore("@") ?: "Unknown",
            answers = answers,
            correctCount = answers.count { it.isCorrect },
            totalQuestions = quiz.questions.size,
            score = score,
            timeTakenSeconds = timeTaken,
            submittedAt = Timestamp.now()
        )

        quizRepo.saveQuizResult(
            result,
            onSuccess = {
                Log.d("QuizSubmission", "Lưu kết quả thành công")
                onSuccess()
            },
            onError = {
                Log.e("QuizSubmission", "Lỗi lưu kết quả: ${it.message}")
                onError(it)
            }
        )
    }
}
