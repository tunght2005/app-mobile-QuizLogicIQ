package com.example.logiciq.data.model


data class ClassItem(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val createdBy: String = "",
    val creatorName: String = "",
    val createdAt: com.google.firebase.Timestamp? = null
)

data class SubjectItem(
    val id: String = "",
    val title: String = "",
    val term: String = "",
    val definition: String = "",
    val createdBy: String = "",
    val creatorName: String ="",
    val createdAt: com.google.firebase.Timestamp? = null
)
