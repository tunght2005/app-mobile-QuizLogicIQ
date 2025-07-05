package com.example.logiciq.data.model

import com.google.firebase.Timestamp
import java.util.Date

data class QuizResult(
    val quizId: String,
    val userId: String,
    val userEmail: String? = null,
    val userName: String? = null,
    val answers: List<Answer>,
    val correctCount: Int,
    val totalQuestions: Int,
    val score: Int,
    val timeTakenSeconds: Long,
    val submittedAt: Timestamp = Timestamp.now()
)
