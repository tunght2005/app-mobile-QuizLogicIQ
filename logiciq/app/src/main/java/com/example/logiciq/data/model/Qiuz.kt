package com.example.logiciq.data.model

import com.google.firebase.Timestamp
import java.util.UUID

data class Quiz(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",                         // ✅ thêm mặc định
    val questions: List<Question> = emptyList(),    // ✅ thêm mặc định
    val maxDurationSeconds: Int = 0,                // ✅ thêm mặc định
    val createByName: String = "",
    val createBy: String = "",
    val createdAt: Timestamp = Timestamp.now()
)
