package com.example.logiciq.view.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logiciq.data.model.Reminder

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReminderSection(
    reminderList: List<Reminder>,
    onAddReminder: (String, String, String) -> Unit,
    onDeleteReminder: (Reminder) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedReminderId by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        Text("Lời Nhắc Nhở", fontWeight = FontWeight.W600, fontSize = 20.sp)

        if (reminderList.isEmpty()) {
            Text(
                text = "Chưa có lời nhắc nào ^_^",
                color = Color.Red,
                fontWeight = FontWeight.W300,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 60.dp, end = 60.dp, top = 20.dp)
            )
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reminderList) { reminder ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF8572FF)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .height(75.dp)
                        .width(330.dp)
                        .combinedClickable(
                            onClick = { /* click nếu cần */ },
                            onLongClick = {
                                selectedReminderId =
                                    if (selectedReminderId == reminder.id) null else reminder.id
                            }
                        )
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier
                                .matchParentSize()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(
                                        color = Color(0xFFBAB0F9),
                                        shape = RoundedCornerShape(16.dp)
                                    ),
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
                                Text(
                                    reminder.title,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.widthIn(max = 200.dp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(30.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${reminder.startTime} - ${reminder.endTime}",
                                        color = Color.White,
                                        fontWeight = FontWeight.W600,
                                        fontSize = 20.sp
                                    )
                                }
                            }
                        }

                        if (selectedReminderId == reminder.id) {
                            IconButton(
                                onClick = {
                                    onDeleteReminder(reminder)
                                    selectedReminderId = null
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Xoá",
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }

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
                    onAddReminder(title, start, end)
                    showDialog = false
                }
            )
        }
    }
}
