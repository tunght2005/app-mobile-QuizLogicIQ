package com.example.logiciq.data.model

import com.google.firebase.Timestamp

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val createdAt: Timestamp? = null,
    val lastLogin: Timestamp? = null
)