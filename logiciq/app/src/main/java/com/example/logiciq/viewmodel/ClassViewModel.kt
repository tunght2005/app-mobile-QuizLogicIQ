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

    fun createClass(
        name: String,
        description: String,
        quiz: Quiz,
        onResult: (Result<String>) -> Unit
    ) {
        viewModelScope.launch {
            val result = repository.createClass(name, description, quiz)
            onResult(result)
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
}
