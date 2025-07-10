package com.example.logiciq.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.example.logiciq.data.model.Reminder
import com.example.logiciq.worker.ReminderWorker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ReminderViewModel(private val context: Context) : ViewModel() {

    private val _reminders = mutableStateListOf<Reminder>()
    val reminders: List<Reminder> = _reminders

    init {
        loadRemindersForToday()
    }

    fun addReminder(title: String, start: String, end: String) {
        val newReminder = Reminder(
            id = UUID.randomUUID().toString(),
            title = title,
            startTime = start,
            endTime = end,
            date = getTodayDate(),
            userId = getCurrentUserId()
        )
        _reminders.add(newReminder)

        // Lưu vào Firestore
        FirebaseFirestore.getInstance()
            .collection("reminders")
            .document(newReminder.id)
            .set(newReminder)

        // Đặt lịch nhắc
        scheduleReminderNotification(newReminder)
    }

    private fun scheduleReminderNotification(reminder: Reminder) {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = formatter.parse(reminder.startTime) ?: return

        val calendar = Calendar.getInstance().apply {
            time = date
            val today = Calendar.getInstance()
            set(Calendar.YEAR, today.get(Calendar.YEAR))
            set(Calendar.MONTH, today.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH))
            add(Calendar.MINUTE, -5)
        }

        val delayMillis = calendar.timeInMillis - System.currentTimeMillis()
        if (delayMillis <= 0) return

        val data = workDataOf("title" to reminder.title)

        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    fun loadRemindersForToday() {
        val db = FirebaseFirestore.getInstance()
        db.collection("reminders")
            .whereEqualTo("userId", getCurrentUserId())
            .whereEqualTo("date", getTodayDate())
            .get()
            .addOnSuccessListener { result ->
                _reminders.clear()
                for (doc in result.documents) {
                    doc.toObject(Reminder::class.java)?.let {
                        _reminders.add(it)
                    }
                }
            }
    }

    private fun getTodayDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getCurrentUserId(): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: "unknown"
    }
}
