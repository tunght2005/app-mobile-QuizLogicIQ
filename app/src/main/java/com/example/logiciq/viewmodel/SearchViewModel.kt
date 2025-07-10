package com.example.logiciq.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logiciq.data.model.SearchResult
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SearchViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResults: StateFlow<List<SearchResult>> = _searchResults.asStateFlow()

    fun search(keyword: String) {
        viewModelScope.launch {
            val resultList = mutableListOf<SearchResult>()
            val addedIds = mutableSetOf<String>()
            try {
                // Tìm lớp học (lọc client)
                val classSnapshot = db.collection("classes").get().await()
                for (doc in classSnapshot.documents) {
                    val name = doc.getString("name") ?: continue
                    if (name.contains(keyword, ignoreCase = true)) {
                        val id = doc.id
                        if (addedIds.add("class_$id")) {
                            val creator = doc.getString("creatorName") ?: "?"
                            val members = (doc.get("members") as? List<*>)?.size ?: 0
                            resultList.add(
                                SearchResult(
                                    id = id,
                                    type = "Lớp Học",
                                    title = name,
                                    subtitle = "$members members",
                                    user = creator
                                )
                            )
                        }
                    }
                }

                // Tìm bài thi (lọc client)
                // Tìm bài thi (lọc client)
                val quizSnapshot = db.collection("quizzes").get().await()
                for (doc in quizSnapshot.documents) {
                    val title = doc.getString("title") ?: continue
                    if (title.contains(keyword, ignoreCase = true)) {
                        val id = doc.id
                        if (addedIds.add("quiz_$id")) {
                            val creator = doc.getString("createByName") ?: "?"
                            val questions = doc.get("questions") as? List<*> ?: emptyList<Any>()
                            val questionCount = questions.size
                            resultList.add(
                                SearchResult(
                                    id = id,
                                    type = "Bài Thi",
                                    title = title,
                                    subtitle = "$questionCount câu hỏi",
                                    user = creator
                                )
                            )
                        }
                    }
                }


                Log.d("SearchViewModel", "✅ Found ${resultList.size} kết quả cho từ khóa \"$keyword\"")
                _searchResults.value = resultList
            } catch (e: Exception) {
                Log.e("SearchViewModel", "❌ Lỗi khi search: ${e.message}")
            }
        }
    }
}