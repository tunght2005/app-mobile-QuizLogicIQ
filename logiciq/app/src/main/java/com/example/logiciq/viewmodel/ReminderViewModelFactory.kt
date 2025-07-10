package com.example.logiciq.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ReminderViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReminderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReminderViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
