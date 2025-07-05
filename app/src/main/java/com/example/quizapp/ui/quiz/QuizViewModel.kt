package com.example.logiciq.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.logiciq.data.model.*
import com.example.logiciq.data.repository.QuizRepository

class QuizViewModel(
    private val repo: QuizRepository = QuizRepository()
) : ViewModel() {

    private val _currentQuiz = MutableLiveData<Quiz>()
    val currentQuiz: LiveData<Quiz> = _currentQuiz

    private val _answers = MutableLiveData<List<Answer>>(emptyList())
    val answers: LiveData<List<Answer>> = _answers

    private val _timeTaken = MutableLiveData<Long>()
    val timeTaken: LiveData<Long> = _timeTaken

    fun loadQuizFromFirestore(quizId: String) {
        repo.getQuizById(
            quizId,
            onSuccess = { _currentQuiz.value = it },
            onError = { e -> Log.e("QuizVM", "Lỗi tải quiz: ${e.message}") }
        )
    }

    fun submitAnswer(questionId: String, selected: String) {
        val question = _currentQuiz.value?.questions?.find { it.id == questionId } ?: return

        val isCorrect = when (question) {
            is Question.Type2 -> selected == question.correctAnswer
            is Question.Type4 -> selected == question.options[question.correctOption]
        }

        val updatedAnswers = _answers.value.orEmpty().toMutableList().apply {
            removeAll { it.questionId == questionId }
            add(Answer(questionId, selected, isCorrect))
        }
        _answers.value = updatedAnswers
    }

    fun finishQuiz(user: User) {
        val quiz = _currentQuiz.value ?: return
        val ansList = _answers.value ?: return
        val correctCount = ansList.count { it.isCorrect }
        val totalQuestions = quiz.questions.size
        val duration = _timeTaken.value ?: 0L

        val result = QuizResult(
            quizId = quiz.id,
            userId = user.uid,
            userEmail = user.email,
            userName = user.name,
            answers = ansList,
            correctCount = correctCount,
            totalQuestions = totalQuestions,
            score = correctCount * 10,
            timeTakenSeconds = duration
        )

        repo.saveQuizResult(
            result,
            onSuccess = { Log.d("QuizVM", "Lưu kết quả thành công") },
            onError = { e -> Log.e("QuizVM", "Lỗi lưu kết quả: ${e.message}") }
        )
    }

    fun updateTimeTaken(millis: Long) {
        _timeTaken.value = millis
    }

    fun saveQuiz(
        quiz: Quiz,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        repo.saveQuiz(quiz, onSuccess, onError)
    }
}