package com.example.logiciq.data.model

import com.google.firebase.Timestamp
import java.util.UUID

data class Quiz(
    val id: String = UUID.randomUUID().toString(),
    val createdBy: String = "",              // ID người tạo quiz
    val createdByName: String = "",            // Tên người tạo (dùng để hiển thị)
    val classIds: List<String> = emptyList(),  // Danh sách class mà người tạo quiz đang là thành viên
    val title: String = "",
    val questions: List<Question> = emptyList(),
    val maxDurationSeconds: Int = 0,
    val createdAt: Timestamp = Timestamp.now()
)
