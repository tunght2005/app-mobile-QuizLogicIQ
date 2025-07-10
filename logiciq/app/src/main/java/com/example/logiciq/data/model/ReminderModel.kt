package com.example.logiciq.data.model

data class Reminder(
    val id: String = "",             // ID của reminder
    val title: String = "",          // Tiêu đề nhắc nhở
    val startTime: String = "",      // Thời gian bắt đầu (ISO 8601 hoặc HH:mm)
    val endTime: String = "",        // Thời gian kết thúc
    val date: String = "",           // Ngày của nhắc nhở (yyyy-MM-dd)
    val userId: String = "",         // ID người dùng (dùng để lọc nhắc nhở của người dùng cụ thể)
)
