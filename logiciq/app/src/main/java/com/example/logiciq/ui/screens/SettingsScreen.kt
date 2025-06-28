package com.example.logiciq.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import com.example.logiciq.R
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.logiciq.ui.components.SettingField

@Composable
fun SettingsScreen(
    navController: NavController,
    onLogoutClick: () -> Unit = {}
) {
    var userName by remember { mutableStateOf("demoname") }
    var email by remember { mutableStateOf("demo@gmail.com") }
    var password by remember { mutableStateOf("***********") }
    var showSaveMessage by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E293B))
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 58.dp, start = 6.dp, end = 6.dp, bottom = 40.dp)
        ) {
            IconButton(
                onClick = {navController.popBackStack()},
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(40.dp),
                    tint = Color.White
                )
            }
            Text(
                text = "Cài Đặt Của Bạn",
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Thông tin cá nhân",
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp, bottom = 35.dp),
            textAlign = TextAlign.Start
        )

        SettingField(
            label = "Tên người dùng",
            value = userName,
            icon = Icons.Default.ArrowForward,
            onValueChange = { userName = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        SettingField(
            label = "Email",
            value = email,
            icon = Icons.Default.ArrowForward,
            onValueChange = { email = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        SettingField(
            label = "Mật khẩu",
            value = password,
            icon = Icons.Default.ArrowForward,
            onValueChange = { password = it }
        )
        Spacer(modifier = Modifier.height(10.dp))
        // Nút Lưu
        Button(
            onClick = {
                showSaveMessage = true
                // TODO: Gọi ViewModel nếu cần lưu backend
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Lưu Thay Đổi", color = Color.White, fontWeight = FontWeight.Bold)
        }
        if (showSaveMessage) {
            Text(
                text = "Thông tin đã được lưu!",
                color = Color.Green,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(36.dp))

        Button(
            onClick = onLogoutClick, // Xử lí viewModel cho nút (đây nha Dũng)
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            modifier = Modifier.width(150.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(2.dp, Color.White)
        ) {
            Text("Đăng Xuất", color = Color.White)
        }

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(R.drawable.logic_iq),
            contentDescription = "Logo",
            modifier = Modifier
                .size(400.dp)
                .padding(bottom = 100.dp)
        )
    }
}
