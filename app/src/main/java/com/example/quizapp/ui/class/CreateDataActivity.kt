package com.example.logiciq.ui.`class`

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.logiciq.data.model.ClassItem
import com.example.logiciq.data.model.SubjectItem
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class FirestoreTestActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Test gọi API tạo dữ liệu thực
        scope.launch {
            fakeCreateClass()
            fakeCreateSubject()
        }
    }

    private suspend fun fakeCreateClass() {
        val user = auth.currentUser ?: return logAndToast("Bạn chưa đăng nhập")
        val uid = user.uid
        val nameUser = user.displayName ?: user.email ?: "Unknown"

        val className = "Lớp Kotlin Android"
        val description = "Lập trình Android nâng cao"

        val doc = db.collection("classes").document()
        val classItem = ClassItem(
            id = doc.id,
            name = className,
            description = description,
            createdBy = uid,
            creatorName = nameUser,
            createdAt = Timestamp.now()
        )

        try {
            doc.set(classItem).await()
            logAndToast("Tạo lớp thành công: ${doc.id}")
        } catch (e: Exception) {
            logAndToast("Lỗi tạo lớp: ${e.message}")
        }
    }

    private suspend fun fakeCreateSubject() {
        val user = auth.currentUser ?: return logAndToast("Bạn chưa đăng nhập")
        val uid = user.uid
        val nameUser = user.displayName ?: user.email ?: "Unknown"

        val title = "OOP"
        val term = "Polymorphism"
        val definition = "Cho phép đối tượng xử lý theo nhiều cách khác nhau."

        val doc = db.collection("subjects").document()
        val subjectItem = SubjectItem(
            id = doc.id,
            title = title,
            term = term,
            definition = definition,
            createdBy = uid,
            creatorName = nameUser,
            createdAt = Timestamp.now()
        )

        try {
            doc.set(subjectItem).await()
            logAndToast("Tạo học phần thành công: ${doc.id}")
        } catch (e: Exception) {
            logAndToast("Lỗi tạo học phần: ${e.message}")
        }
    }

    private fun logAndToast(msg: String) {
        Log.d("FirestoreTest", msg)
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
