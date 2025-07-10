package com.example.logiciq.view.components

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CreateReminderDialog(
    onDismiss: () -> Unit,
    onSave: (title: String, startTime: String, endTime: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    val context = LocalContext.current

    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    // Hiển thị TimePicker cho giờ bắt đầu
    LaunchedEffect(showStartPicker) {
        if (showStartPicker) {
            TimePickerDialog(
                context,
                { _, hour: Int, minute: Int ->
                    startTime = String.format("%02d:%02d", hour, minute)
                },
                12, 0, true
            ).show()
            showStartPicker = false
        }
    }

    // Hiển thị TimePicker cho giờ kết thúc
    LaunchedEffect(showEndPicker) {
        if (showEndPicker) {
            TimePickerDialog(
                context,
                { _, hour: Int, minute: Int ->
                    endTime = String.format("%02d:%02d", hour, minute)
                },
                14, 0, true
            ).show()
            showEndPicker = false
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Tạo Lịch Mới", fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Tiêu đề") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { showStartPicker = true }) {
                        Text("Chọn giờ bắt đầu")
                    }
                    Text(
                        text = startTime.ifEmpty { "--:--" },
                        modifier = Modifier.alignByBaseline()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { showEndPicker = true }) {
                        Text("Chọn giờ kết thúc")
                    }
                    Text(
                        text = endTime.ifEmpty { "--:--" },
                        modifier = Modifier.alignByBaseline()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && startTime.isNotBlank() && endTime.isNotBlank()) {
                        onSave(title, startTime, endTime)
                        onDismiss()
                    }
                }
            ) {
                Text("Lưu")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}
