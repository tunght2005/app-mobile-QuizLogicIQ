package com.example.logiciq.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logiciq.data.model.Question
import com.example.logiciq.data.model.Quiz
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.google.firebase.Timestamp

class TestViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _testList = MutableStateFlow<List<Quiz>>(emptyList())
    val testList = _testList.asStateFlow()

    fun loadTests() {
        viewModelScope.launch {
            db.collection("tests").get().addOnSuccessListener { result ->
                val quizzes = result.mapNotNull { doc ->
                    try {
                        val title = doc.getString("title") ?: return@mapNotNull null
                        val questionsData = doc.get("questions") as? List<Map<String, Any>> ?: return@mapNotNull null

                        val questions = questionsData.mapNotNull { q ->
                            val question = q["question"] as? String ?: return@mapNotNull null
                            val answers = q["answers"] as? List<String> ?: return@mapNotNull null
                            if (answers.size != 4) return@mapNotNull null

                            Question.Type4(
                                text = question,
                                optionA = answers[0],
                                optionB = answers[1],
                                optionC = answers[2],
                                optionD = answers[3],
                                correctOption = 'A' // ✅ Nếu bạn chưa lưu đáp án đúng, mặc định 'A'
                            )
                        }

                        Quiz(
                            title = title,
                            questions = questions,
                            maxDurationSeconds = 600,
                            createdAt = doc.getTimestamp("timestamp") ?: Timestamp.now()
                        )
                    } catch (e: Exception) {
                        null
                    }
                }

                _testList.value = quizzes
            }
        }
    }
}
