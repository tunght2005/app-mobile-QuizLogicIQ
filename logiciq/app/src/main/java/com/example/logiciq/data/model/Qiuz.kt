package com.example.logiciq.data.model

import com.google.firebase.Timestamp
import java.util.UUID

data class Quiz(
    val id: String = UUID.randomUUID().toString(),
    val classId: String? = null,
    val title: String = "",
    val questions: List<Question> = emptyList(),
    val maxDurationSeconds: Int = 0,
    val createByName: String = "",
    val createBy: String = "",
    val createdAt: Timestamp = Timestamp.now()
)

