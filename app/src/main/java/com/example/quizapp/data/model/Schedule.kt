package com.example.logiciq.data.model

data class ScheduleItem(
    val id: String = "",
    val title: String = "",
    val startTime: Long,
    val endTime: Long,
    val reminderMinutesBefore: Int = 15,

    val createdBy: String = "",
    val creatorName: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long ?= null
)