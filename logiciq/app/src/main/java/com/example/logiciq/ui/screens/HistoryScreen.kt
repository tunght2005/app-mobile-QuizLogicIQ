package com.example.logiciq.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.logiciq.R

@Composable
fun HistoryScreen(
    navController: NavController,
    notifications: List<String> = listOf(
        "Bạn đã hoàn thành bài kiểm tra IQ!",
        "Cập nhật tính năng mới: Bổ sung dạng câu hỏi logic.",
        "Lịch học mới đã được thêm vào ngày mai.",
        "Lịch học mới đã được thêm vào ngày mai.",
        "Lịch học mới đã được thêm vào ngày mai.",
        "Lịch học mới đã được thêm vào ngày mai.",
        "Lịch học mới đã được thêm vào ngày mai.",
        "Lịch học mới đã được thêm vào ngày mai.",
        "Lịch học mới đã được thêm vào ngày mai.",
        "Lịch học mới đã được thêm vào ngày mai.",
        "Bạn vừa đăng nhập vào hệ thống lúc 14:05."
    )
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E293B))
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 58.dp, bottom = 30.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
            Text(
                text = "Lịch Sử Hoạt Động",
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Text(
            text = "Thông báo gần đây",
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            textAlign = TextAlign.Start
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(notifications) { msg ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF3C4A64)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = msg,
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        Image(
            painter = painterResource(R.drawable.logic_iq),
            contentDescription = "Logo",
            modifier = Modifier
                .size(250.dp)
                .padding(top = 16.dp, bottom = 32.dp)
        )
    }
}
