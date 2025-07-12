package com.example.logiciq.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logiciq.data.model.Quiz
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewClassScreen(
    navController: androidx.navigation.NavController,
    onBack: () -> Unit,
    onSave: (String, String) -> Unit // ✅ Thêm tham số onSave rõ ràng
) {
    var className by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E293B))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 58.dp, start = 30.dp, end = 30.dp, bottom = 40.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Text(
                text = "Tạo lớp mới",
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
                fontSize = 25.sp,
                fontWeight = FontWeight.Medium
            )

            IconButton(
                onClick = {
                    if (className.isNotBlank()) {
                        onSave(className, description) // ✅ Gọi onSave đã truyền từ NavGraph
                    }
                },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        // Input Fields
        Column(
            modifier = Modifier
                .padding(horizontal = 40.dp, vertical = 10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(36.dp)
        ) {
            InputBox(label = "Tên lớp", hint = "Môn học, khóa học, ...", value = className) {
                className = it
            }

            InputBox(label = "Mô tả", hint = "Thông tin bổ sung.", value = description) {
                description = it
            }
        }
    }
}

@Composable
private fun InputBox(
    label: String,
    hint: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.White, RoundedCornerShape(14.dp))
            .background(Color(0xFF3C5A99), RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column {
            Text(
                text = label,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                singleLine = true,
                cursorBrush = SolidColor(Color.White),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(text = hint, color = Color(0xFFDADADA))
                    }
                    innerTextField()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
