package com.example.logiciq.data.model

import com.google.firebase.Timestamp
import java.util.UUID

data class Quiz(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val questions: List<Question>,
    val maxDurationSeconds: Int,
    val createByName: String = "",
    val createBy: String = "",
    val createdAt: Timestamp = Timestamp.now()
)
