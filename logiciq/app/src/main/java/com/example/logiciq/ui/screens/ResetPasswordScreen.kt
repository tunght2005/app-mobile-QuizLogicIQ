package com.example.logiciq.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.logiciq.R
import com.example.logiciq.navigation.Routes

@Composable
fun ResetPasswordScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF3F6ABA))
            .padding(34.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logic_iq),
            contentDescription = "Logic IQ Logo",
            modifier = Modifier
                .size(320.dp)
        )
        Text("Chúng tôi sẽ gửi mật khẩu qua gmail của bạn để cài lại tài khoản!",
            style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 30.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .offset(x = 0.dp, y = (-70).dp)
                .padding(start = 16.dp, end = 16.dp, top = 30.dp, bottom = 0.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clickable { navController.popBackStack() }
        ) {
            Text(
                text = "Quay lại",
                fontSize = 18.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(35.dp)
            )
        }

        var email by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email tài khoản của bạn", color = Color.Gray)},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Icon",
                    tint = Color(0xFFFFFFFF),
                    modifier = Modifier.size(35.dp)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xCCCCCCCC),
                            Color(0xFF8DAAEE),
                            Color(0xFF8DAAEE)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Xử lý gửi mật khẩu qua email */ },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF8DAAEE)
            )
            ) {
            Text("Gửi mật khẩu",
                fontSize = 25.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                textAlign = TextAlign.Center
            )
        }
        Row(
            modifier = Modifier
                .padding(top = 5.dp)
        ) {
            Text("Bạn có thể tạo tài khoản?", fontSize = 16.sp, color = Color.White)
            Text("Đăng ký",
                fontSize = 18.sp,
                color = Color.White,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 2.dp)
                    .clickable {
                        navController.navigate(Routes.REGISTER) }
            )
        }
    }
}