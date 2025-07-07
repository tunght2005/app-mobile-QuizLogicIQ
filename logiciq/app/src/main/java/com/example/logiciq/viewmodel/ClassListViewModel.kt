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

class ClassListViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _classList = MutableStateFlow<List<ClassItem>>(emptyList())
    val classList: StateFlow<List<ClassItem>> = _classList.asStateFlow()

    fun loadAllClasses() {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("classes").get().await()
                val classes = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(ClassItem::class.java)?.copy(id = doc.id)
                }
                _classList.value = classes
            } catch (e: Exception) {
                // Xử lý lỗi nếu cần
            }
        }
    }
}
