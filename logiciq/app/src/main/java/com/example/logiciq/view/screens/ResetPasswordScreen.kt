package com.example.logiciq.view.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.logiciq.R
import com.example.logiciq.navigation.Routes
import com.example.logiciq.view.components.LoadingOverlay
import com.example.logiciq.viewmodel.AuthViewModel
import com.example.logiciq.viewmodel.ResetPasswordResult

@Composable
fun ResetPasswordScreen(navController: NavController, viewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current
    var email by rememberSaveable { mutableStateOf("") }
    val resetState by viewModel.resetPasswordState.collectAsState()

    // Xử lý kết quả gửi email
    LaunchedEffect(resetState) {
        when (resetState) {
            is ResetPasswordResult.Success -> {
                Toast.makeText(context, "Đã gửi email khôi phục mật khẩu!", Toast.LENGTH_LONG).show()
                viewModel.resetResetPasswordState()
                navController.popBackStack()
            }

            is ResetPasswordResult.Error -> {
                val msg = (resetState as ResetPasswordResult.Error).message
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                viewModel.resetResetPasswordState()
            }

            else -> Unit
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                modifier = Modifier.size(320.dp)
            )

            Text(
                "Chúng tôi sẽ gửi mật khẩu qua gmail của bạn để cài lại tài khoản!",
                style = TextStyle(
                    fontSize = 20.sp,
                    lineHeight = 30.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .offset(y = (-70).dp)
                    .padding(horizontal = 16.dp, vertical = 30.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clickable { navController.popBackStack() }
            ) {
                Text("Quay lại", fontSize = 18.sp, color = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(35.dp))
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email tài khoản của bạn", color = Color.Gray) },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = "Email Icon", tint = Color.White, modifier = Modifier.size(35.dp))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            listOf(Color(0xCCCCCCCC), Color(0xFF8DAAEE), Color(0xFF8DAAEE)),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (email.isNotBlank()) {
                        viewModel.sendResetPasswordEmail(email)
                    } else {
                        Toast.makeText(context, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show()
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8DAAEE)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Gửi mật khẩu",
                    fontSize = 25.sp,
                    modifier = Modifier.padding(10.dp),
                    textAlign = TextAlign.Center
                )
            }

            Row(modifier = Modifier.padding(top = 5.dp)) {
                Text("Bạn có thể tạo tài khoản?", fontSize = 16.sp, color = Color.White)
                Text(
                    "Đăng ký",
                    fontSize = 18.sp,
                    color = Color.White,
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .clickable { navController.navigate(Routes.REGISTER) }
                )
            }
        }

        // 👉 Loading overlay khi đang gửi email
        if (resetState is ResetPasswordResult.Loading) {
            LoadingOverlay()
        }
    }
}
