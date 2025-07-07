package com.example.logiciq.view.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReminderSection() {
    //Xử lí viewModel để lấy api cho lịch
    var showDialog by remember { mutableStateOf(false) }
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        Text("Lời Nhắc Nhở", fontWeight = FontWeight.Bold)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            items(4) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF8572FF)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .height(75.dp)
                        .width(330.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(color = Color(0xFFBAB0F9), shape = RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(24.dp))
                        Column {
                            Text("DEMO GHI CHÚ LỊCH", fontWeight = FontWeight.Bold, color = Color.White)
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "12.00 - 16.00",
                                    color = Color.White
                                )
                            }

                        }
                    }
                }
            }
        }
        // Add form xữ lí tạo lịch với modal "CHỈ" lịch trong ngày (update lịch tự lựa chọn)
        Button(
            onClick = { showDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDE496E)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.padding(top = 8.dp, start = 24.dp)
        ) {
            Text("Tạo Lịch", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }

        if (showDialog) {
            CreateReminderDialog(
                onDismiss = { showDialog = false },
                onSave = { title, start, end ->
                    // TODO: Lưu lịch vào ViewModel hoặc danh sách
                    Log.d("Reminder", "$title: $start - $end")
                }
            )
        }

    }
}