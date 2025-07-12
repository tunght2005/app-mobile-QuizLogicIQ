package com.example.logiciq.data.model

import com.google.firebase.Timestamp
import java.util.Date

data class QuizResult(
    val quizId: String = "",
    val userId: String = "",
    val userEmail: String? = null,
    val userName: String? = null,
    val answers: List<Answer> = emptyList(),
    val correctCount: Int = 0,
    val totalQuestions: Int = 0,
    val score: Int = 0,
    val timeTakenSeconds: Long = 0L,
    val submittedAt: Timestamp = Timestamp.now()
)
fun UiQuestion.toAnswer(selected: String): Answer {
    return Answer(
        questionId = id,
        selected = selected,
        isCorrect = selected == correctAnswer
    )
}
data class UserScore(
    val userId: String,
    val name: String,
    val correct: Int,
    val wrong: Int
)

fun QuizResult.toUserScore(): UserScore {
    return UserScore(
        userId = userId,
        name = userName ?: "Người dùng",
        correct = correctCount,
        wrong = totalQuestions - correctCount
    )
}
