package com.example.logiciq.ui.`class`

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logiciq.data.repository.ClassRepository
import com.example.logiciq.data.repository.SubjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClassViewModel(
    private val repo: ClassRepository = ClassRepository()
) : ViewModel() {
    private val _createResult = MutableStateFlow<Result<String>?>(null)
    val createResult: StateFlow<Result<String>?> = _createResult

    fun createClass(name: String, description: String) {
        viewModelScope.launch {
            _createResult.value = repo.createClass(name, description)
        }
    }
}

class SubjectViewModel(
    private val repo: SubjectRepository = SubjectRepository()
) : ViewModel() {
    private val _createResult = MutableStateFlow<Result<String>?>(null)
    val createResult: StateFlow<Result<String>?> = _createResult

    fun createSubject(title: String, term: String, definition: String) {
        viewModelScope.launch {
            _createResult.value = repo.createSubject(title, term, definition)
        }
    }
}
