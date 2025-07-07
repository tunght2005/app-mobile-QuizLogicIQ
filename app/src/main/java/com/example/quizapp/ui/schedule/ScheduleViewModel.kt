package com.example.logiciq.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logiciq.data.repository.ScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val repo: ScheduleRepository = ScheduleRepository()
) : ViewModel() {

    private val _createResult = MutableStateFlow<Result<String>?>(null)
    val createResult: StateFlow<Result<String>?> = _createResult

    fun createSchedule(
        title: String,
        startTime: Long,
        endTime: Long,
        reminderMinutesBefore: Int
    ) {
        viewModelScope.launch {
            val result = repo.createSchedule(title, startTime, endTime, reminderMinutesBefore)
            _createResult.value = result
        }
    }
}
