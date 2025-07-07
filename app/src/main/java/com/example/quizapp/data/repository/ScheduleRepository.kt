package com.example.logiciq.data.repository

import com.example.logiciq.data.model.ScheduleItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ScheduleRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val col = db.collection("schedules")

    suspend fun createSchedule(
        title: String,
        startTime: Long,
        endTime: Long,
        reminderMinutesBefore: Int
    ): Result<String> {
        val user = FirebaseAuth.getInstance().currentUser
            ?: return Result.failure(Exception("User not logged in"))

        val doc = col.document()

        val item = ScheduleItem(
            id = doc.id,
            title = title,
            startTime = startTime,
            endTime = endTime,
            reminderMinutesBefore = reminderMinutesBefore,
            createdBy = user.uid,
            creatorName = user.displayName ?: user.email ?: "Unknown",
            createdAt = System.currentTimeMillis()
        )

        return try {
            doc.set(item).await()
            Result.success(doc.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
