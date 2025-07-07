package com.example.logiciq.data.model

import com.google.firebase.Timestamp

data class ClassItem(
    val id: String = "",
    val name: String = "",
    val memberCount: Int = 0, // ✅ Bổ sung giá trị mặc định
    val members: List<Member> = emptyList(),
    val quiz: Quiz = Quiz(), // ✅ Phải có constructor mặc định
    val description: String = "",
    val createdBy: String = "",
    val creatorName: String = "",
    val createdAt: Timestamp? = null
)

data class Member(
    val userId: String = "",
    val userName: String = "",
    val avatar: String? = null
)
