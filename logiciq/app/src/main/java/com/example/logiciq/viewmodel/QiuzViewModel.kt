package com.example.logiciq.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logiciq.data.model.Quiz
import com.example.logiciq.data.model.QuizResult
import com.example.logiciq.data.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizViewModel(
    private val repository: QuizRepository = QuizRepository()
) : ViewModel() {

    private val _quizList = MutableStateFlow<List<Quiz>>(emptyList())
    val quizList: StateFlow<List<Quiz>> = _quizList.asStateFlow()

    private val _currentQuiz = MutableStateFlow<Quiz?>(null)
    val currentQuiz: StateFlow<Quiz?> = _currentQuiz.asStateFlow()

    // ✅ Tạo mới bài thi
    fun createQuiz(quiz: Quiz, onResult: (Boolean, String?) -> Unit) {
        repository.saveQuiz(
            quiz = quiz,
            onSuccess = { onResult(true, null) },
            onError = { e -> onResult(false, e.message) }
        )
    }

    // ✅ Lấy danh sách tất cả bài thi (cho LibraryScreen)
    fun loadAllQuizzes() {
        repository.getAllQuizzes(
            onSuccess = { list -> _quizList.value = list },
            onError = { e -> println("❌ Lỗi load quiz: ${e.message}") }
        )
    }

    // ✅ Lấy quiz theo ID
    fun loadQuizById(id: String) {
        repository.getQuizById(
            quizId = id,
            onSuccess = { quiz -> _currentQuiz.value = quiz },
            onError = { e -> println("❌ Quiz không tồn tại: ${e.message}") }
        )
    }

    // ✅ Lưu kết quả làm bài
    fun saveQuizResult(result: QuizResult, onResult: (Boolean, String?) -> Unit) {
        repository.saveQuizResult(
            result = result,
            onSuccess = { onResult(true, null) },
            onError = { e -> onResult(false, e.message) }
        )
    }
}
