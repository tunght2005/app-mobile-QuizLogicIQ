package com.example.logiciq.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logiciq.data.model.ClassItem
import com.example.logiciq.data.model.Quiz
import com.example.logiciq.data.repository.ClassRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClassViewModel(
    private val repository: ClassRepository = ClassRepository()
) : ViewModel() {

    private val _classState = MutableStateFlow<ClassItem?>(null)
    val classState: StateFlow<ClassItem?> = _classState.asStateFlow()

    private val _quizList = MutableStateFlow<List<Quiz>>(emptyList())
    val quizList: StateFlow<List<Quiz>> = _quizList.asStateFlow()

    // ✅ KHÔNG cần truyền quiz nữa
    fun createClass(
        name: String,
        description: String,
        onResult: (Result<String>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val id = repository.createClass(name, description)
                onResult(Result.success(id))
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        }
    }

    fun loadClass(classId: String) {
        viewModelScope.launch {
            try {
                val classItem = repository.getClassById(classId)
                _classState.value = classItem
            } catch (e: Exception) {
                Log.e("ClassViewModel", "Lỗi tải lớp học: ${e.message}")
            }
        }
    }

    fun loadQuizzes(classId: String) {
        viewModelScope.launch {
            try {
                val quizzes = repository.getQuizzesByClassId(classId)
                _quizList.value = quizzes
            } catch (e: Exception) {
                Log.e("ClassViewModel", "Lỗi tải bài thi của lớp: ${e.message}")
            }
        }
    }
}
