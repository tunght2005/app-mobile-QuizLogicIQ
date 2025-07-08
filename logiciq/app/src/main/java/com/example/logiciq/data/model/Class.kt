package com.example.logiciq.data.model

import com.google.firebase.Timestamp

data class ClassItem(
    val id: String = "",
    val name: String = "",
    val members: List<Member> = emptyList(),

    val quizzes: List<Quiz> = emptyList(),

    val description: String = "",
    val createdBy: String = "",
    val creatorName: String = "",
    val createdAt: Timestamp? = null
){
    val memberCount: Int
        get()= members.size
}

data class Member(
    val userId: String = "",
    val userName: String = "",
    val avatar: String? = null
)
