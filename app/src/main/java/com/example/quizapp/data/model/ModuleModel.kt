package com.example.logiciq.data.model

data class ModuleModel(
    val id: String = "",
    val classId: String = "",
    val title: String = "",
    val terms: List<Term> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

