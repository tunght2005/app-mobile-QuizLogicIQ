package com.example.logiciq.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logiciq.data.model.ClassItem
import com.example.logiciq.data.model.Quiz
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LibraryViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _classList = MutableStateFlow<List<ClassItem>>(emptyList())
    val classList: StateFlow<List<ClassItem>> = _classList.asStateFlow()

    private val _testList = MutableStateFlow<List<Quiz>>(emptyList())
    val testList: StateFlow<List<Quiz>> = _testList.asStateFlow()

    fun loadClasses() {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("classes").get().await()
                val list = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(ClassItem::class.java)?.copy(id = doc.id)
                }
                _classList.value = list
            } catch (e: Exception) {
                Log.e("LibraryViewModel", "❌ Lỗi khi load classes: ${e.message}")
            }
        }
    }

    fun loadTests() {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("quizzes").get().await()
                val list = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Quiz::class.java)?.copy(id = doc.id)
                }
                _testList.value = list
            } catch (e: Exception) {
                Log.e("LibraryViewModel", "❌ Lỗi khi load bài thi: ${e.message}")
            }
        }
    }
}
