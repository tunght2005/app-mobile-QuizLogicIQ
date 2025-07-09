package com.example.logiciq.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logiciq.data.model.ClassItem
import com.example.logiciq.data.model.Question
import com.example.logiciq.data.model.Quiz
import com.example.logiciq.data.model.questionFromMap
import com.google.firebase.Timestamp
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

    fun loadTests(userId: String) {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("quizzes")
                    .whereEqualTo("createBy", userId)
                    .get()
                    .await()

                val list = snapshot.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null

                    val questionsRaw = data["questions"] as? List<Map<String, Any>> ?: emptyList()
                    val questions = questionsRaw.mapNotNull {
                        try {
                            questionFromMap(it) as? Question
                        } catch (e: Exception) {
                            Log.e("LibraryViewModel", "❌ Lỗi convert question: ${e.message}")
                            null
                        }
                    }

                    Quiz(
                        id = doc.id,
                        classIds = data["classIds"] as? List<String> ?: emptyList(),
                        title = data["title"] as? String ?: "",
                        questions = questions,
                        maxDurationSeconds = (data["maxDurationSeconds"] as? Long)?.toInt() ?: 0,
                        createdByName = data["createByName"] as? String ?: "",
                        createdBy = data["createBy"] as? String ?: "",
                        createdAt = data["createdAt"] as? Timestamp ?: Timestamp.now()
                    )
                }

                Log.d("LibraryViewModel", "✅ Đã load ${list.size} bài test cá nhân")
                _testList.value = list
            } catch (e: Exception) {
                Log.e("LibraryViewModel", "❌ Lỗi khi load bài thi cá nhân: ${e.message}")
            }
        }
    }

}