package com.example.logiciq.ui.schedule

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.*

class FakeScheduleActivity : AppCompatActivity() {

    private val viewModel: ScheduleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val calendarStart = Calendar.getInstance().apply {
            set(2025, 6, 6, 9, 0) // 6/7/2025 09:00
        }

        val calendarEnd = Calendar.getInstance().apply {
            set(2025, 6, 6, 10, 30) // 6/7/2025 10:30
        }

        val title = "Lịch học Kotlin + Firebase"
        val startMillis = calendarStart.timeInMillis
        val endMillis = calendarEnd.timeInMillis
        val reminderBefore = 15 // phút

        // Gọi tạo schedule
        viewModel.createSchedule(title, startMillis, endMillis, reminderBefore)

        // Observe kết quả
        lifecycleScope.launch {
            viewModel.createResult.collect { result ->
                result?.onSuccess {
                    toast("Tạo lịch thành công: $it")
                    Log.d("ScheduleFake", "Tạo thành công: $it")
                }?.onFailure {
                    toast("Lỗi: ${it.message}")
                    Log.e("ScheduleFake", "Lỗi: ${it.message}")
                }
            }
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
