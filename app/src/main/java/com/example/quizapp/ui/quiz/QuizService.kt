package com.example.logiciq.backend

import android.util.Log
import com.example.logiciq.data.model.Question
import com.example.logiciq.data.model.Quiz
import com.example.logiciq.data.repository.QuizRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth

object QuizService {
    private val repo = QuizRepository()

    fun createQuiz(
        title: String,
        questions: List<Question>,
        maxDurationSeconds: Int,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val user = FirebaseAuth.getInstance().currentUser
        val quiz = Quiz(
            title = title,
            questions = questions,
            maxDurationSeconds = maxDurationSeconds,
            createByName = user?.displayName ?: user?.email ?: "unknown",
            createBy = user?.uid ?: "unknown",
            createdAt = Timestamp.now()
        )

        repo.saveQuiz(
            quiz,
            onSuccess = {
                Log.d("QuizService", "Quiz created: ${quiz.title}")
                onSuccess()
            },
            onError = {
                Log.e("QuizService", "Error creating quiz: ${it.message}")
                onError(it)
            }
        )
    }
}
