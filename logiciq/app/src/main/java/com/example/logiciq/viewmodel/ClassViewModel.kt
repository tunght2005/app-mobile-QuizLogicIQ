package com.example.logiciq.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logiciq.data.model.ClassItem
import com.example.logiciq.data.model.Quiz
import com.example.logiciq.data.repository.ClassRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
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

    /**
     * ✅ Tạo lớp mới
     */
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

    fun shareQuizToClass(
        quizId: String,
        classId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.shareQuizToClass(quizId, classId)
                onSuccess()
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }


    /**
     * ✅ Tải thông tin lớp học cụ thể
     */
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

    fun deleteClass(classId: String, onSuccess: () -> Unit) {
        Firebase.firestore.collection("classes")
            .document(classId)
            .delete()
            .addOnSuccessListener { onSuccess() }
    }


    /**
     * ✅ Tải các bài thi thuộc lớp (dựa vào classIds trong Quiz)
     */
    fun loadQuizzes(classId: String) {
        viewModelScope.launch {
            try {
                Log.d("ClassViewModel", "Đang tải quiz cho classId = $classId")
                val quizzes = repository.getQuizzesForClass(classId)
                Log.d("ClassViewModel", "Tải thành công ${quizzes.size} bài thi")
                quizzes.forEach {
                    Log.d("ClassViewModel", "Quiz: ${it.title} - classIds = ${it.classIds}")
                }
                _quizList.value = quizzes
            } catch (e: Exception) {
                Log.e("ClassViewModel", "Lỗi tải bài thi của lớp: ${e.message}", e)
            }
        }
    }

    fun joinClass(classId: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            val db = FirebaseFirestore.getInstance()
            val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
            if (user != null) {
                val memberData = mapOf(
                    "userId" to user.uid,
                    "userName" to (user.displayName ?: "Người dùng"),
                    "avatar" to (user.photoUrl?.toString() ?: "")
                )

                db.collection("classes")
                    .document(classId)
                    .update("members", com.google.firebase.firestore.FieldValue.arrayUnion(memberData))
                    .addOnSuccessListener {
                        Log.d("ClassViewModel", "✅ Đã tham gia lớp thành công")
                        onComplete()
                    }
                    .addOnFailureListener {
                        Log.e("ClassViewModel", "❌ Lỗi khi tham gia lớp: ${it.message}")
                    }
            } else {
                Log.e("ClassViewModel", "❌ Người dùng chưa đăng nhập")
            }
        }
    }

    fun leaveClass(classId: String, onComplete: () -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val memberData = mapOf(
            "userId" to user.uid,
            "userName" to (user.displayName ?: "Người dùng"),
            "avatar" to (user.photoUrl?.toString() ?: "")
        )

        Firebase.firestore.collection("classes")
            .document(classId)
            .update("members", FieldValue.arrayRemove(memberData))
            .addOnSuccessListener {
                Log.d("ClassViewModel", "✅ Rời lớp thành công")
                onComplete()
            }
            .addOnFailureListener {
                Log.e("ClassViewModel", "❌ Lỗi rời lớp: ${it.message}")
            }
    }


}

