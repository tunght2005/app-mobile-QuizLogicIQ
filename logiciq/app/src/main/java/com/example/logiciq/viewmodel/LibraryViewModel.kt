package com.example.logiciq.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logiciq.data.model.ClassItem
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

    init {
        loadClasses()
    }

    // ✅ Cập nhật: từ private ➜ public để có thể gọi từ màn hình UI
    fun loadClasses() {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("classes").get().await()
                val list = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(ClassItem::class.java)?.copy(id = doc.id)
                }
                _classList.value = list
            } catch (e: Exception) {
                println("❌ Lỗi khi load classes: ${e.message}")
            }
        }
    }
}
