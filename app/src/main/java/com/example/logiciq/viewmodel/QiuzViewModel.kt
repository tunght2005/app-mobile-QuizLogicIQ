package com.example.logiciq.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logiciq.data.model.Question
import com.example.logiciq.data.model.Quiz
import com.example.logiciq.data.model.QuizResult
import com.example.logiciq.data.model.UserScore
import com.example.logiciq.data.repository.QuizRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

    private val _userClassIds = MutableStateFlow<List<String>>(emptyList())
    val userClassIds: StateFlow<List<String>> = _userClassIds.asStateFlow()

    private val _leaderboard = MutableStateFlow<List<UserScore>>(emptyList())
    val leaderboard: StateFlow<List<UserScore>> = _leaderboard.asStateFlow()

    fun loadUserClassIds(userId: String) {
        viewModelScope.launch {
            try {
                val ids = repository.getUserClassIds(userId)
                _userClassIds.value = ids
            } catch (e: Exception) {
                println("❌ Lỗi lấy classIds: \${e.message}")
            }
        }
    }

    fun createQuiz(
        userId: String,
        userName: String,
        title: String,
        questions: List<Question>,
        maxDurationSeconds: Int,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val classIds = _userClassIds.value.ifEmpty {
                    repository.getUserClassIds(userId).also { _userClassIds.value = it }
                }

                val quiz = Quiz(
                    createdBy = userId,
                    createdByName = userName,
                    classIds = classIds,
                    title = title,
                    questions = questions,
                    maxDurationSeconds = maxDurationSeconds
                )

                repository.saveQuiz(
                    quiz = quiz,
                    onSuccess = { onResult(true, null) },
                    onError = { e -> onResult(false, e.message) }
                )
            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }

    fun deleteQuiz(quizId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                repository.deleteQuiz(quizId)
                onSuccess()
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun loadUserQuizzes(userId: String) {
        repository.getQuizzesByUserId(
            userId = userId,
            onSuccess = { list -> _quizList.value = list },
            onError = { e -> println("❌ Lỗi khi lấy quiz của user: \${e.message}") }
        )
    }

    fun loadQuizzesByClassId(classId: String) {
        repository.getQuizzesByClassId(
            classId = classId,
            onSuccess = { list -> _quizList.value = list },
            onError = { e -> println("❌ Lỗi load quiz theo classId: \${e.message}") }
        )
    }

    fun loadQuizzesByUserClasses(userId: String) {
        viewModelScope.launch {
            try {
                val classIds = repository.getUserClassIds(userId)
                _userClassIds.value = classIds

                val allQuizzes = mutableListOf<Quiz>()

                classIds.forEach { classId ->
                    repository.getQuizzesByClassId(
                        classId = classId,
                        onSuccess = { list ->
                            allQuizzes.addAll(list)
                            _quizList.value = allQuizzes
                        },
                        onError = { e ->
                            println("❌ Lỗi lấy quiz theo classId '\$classId': \${e.message}")
                        }
                    )
                }
            } catch (e: Exception) {
                println("❌ Lỗi khi lấy classIds của user: \${e.message}")
            }
        }
    }

    fun loadQuizById(id: String) {
        repository.getQuizById(
            quizId = id,
            onSuccess = { quiz -> _currentQuiz.value = quiz },
            onError = { e -> println("❌ Quiz không tồn tại: \${e.message}") }
        )
    }

    fun saveQuizResult(result: QuizResult, onResult: (Boolean, String?) -> Unit) {
        repository.saveQuizResult(
            result = result,
            onSuccess = { onResult(true, null) },
            onError = { e -> onResult(false, e.message) }
        )
    }

    fun saveCurrentResult(userId: String, userName: String, correct: Int, total: Int, quizId: String) {
        val result = QuizResult(
            quizId = quizId,
            userId = userId,
            userName = userName,
            correctCount = correct,
            totalQuestions = total,
            answers = emptyList(),
            score = (correct * 100) / total,
            timeTakenSeconds = 0
        )
        saveQuizResult(result) { success, error ->
            if (success) {
                println("✅ Lưu kết quả thành công")
            } else {
                println("❌ Lỗi lưu kết quả: \$error")
            }
        }
    }

    fun loadLeaderboard(quizId: String) {
        Firebase.firestore.collection("quiz_results")
            .whereEqualTo("quizId", quizId)
            .get()
            .addOnSuccessListener { snapshot ->
                val scores = snapshot.documents
                    .mapNotNull { it.toObject(QuizResult::class.java) }
                    .groupBy { it.userId }
                    .mapValues { entry ->
                        entry.value.maxByOrNull { it.correctCount }!!
                    }
                    .values
                    .map {
                        UserScore(
                            userId = it.userId,
                            name = it.userName ?: "Người dùng",
                            correct = it.correctCount,
                            wrong = it.totalQuestions - it.correctCount
                        )
                    }
                    .sortedByDescending { it.correct }

                _leaderboard.value = scores
            }
            .addOnFailureListener {
                println("❌ Lỗi khi load leaderboard: ${it.message}")
            }
    }


}
