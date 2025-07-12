package com.example.logiciq.view.screens

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.logiciq.R
import com.example.logiciq.navigation.Routes
import com.example.logiciq.view.components.LoadingOverlay
import com.example.logiciq.viewmodel.AuthResult
import com.example.logiciq.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current
    val authState by authViewModel.authState.collectAsState()

    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Xử lý trạng thái từ ViewModel
    LaunchedEffect(authState) {
        when (authState) {
            is AuthResult.Success -> {
                Toast.makeText(context, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.REGISTER) { inclusive = true }
                }
                authViewModel.resetAuthState()
            }

            is AuthResult.Error -> {
                Toast.makeText(context, (authState as AuthResult.Error).message, Toast.LENGTH_LONG).show()
                authViewModel.resetAuthState()
            }

            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF3F6ABA))
                .padding(start = 34.dp, end = 34.dp, bottom = 200.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logic_iq),
                contentDescription = "Logo",
                modifier = Modifier.size(320.dp)
            )

            Text(
                "Đăng ký để tham gia học tập!",
                style = TextStyle(
                    fontSize = 20.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .offset(y = (-100).dp)
                    .padding(start = 34.dp, end = 34.dp, top = 30.dp)
            )

            // Username Field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("Nhập tên đăng nhập", color = Color.White) },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
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
                            start = Offset.Zero,
                            end = Offset.Infinite
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email tài khoản của bạn", color = Color.White) },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null, tint = Color.White)
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
                            start = Offset.Zero,
                            end = Offset.Infinite
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Mật khẩu", color = Color.White) },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White)
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(id = if (passwordVisible) R.drawable.visibility else R.drawable.visibilityoff),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            listOf(Color(0xCCCCCCCC), Color(0xFF8DAAEE), Color(0xFF8DAAEE)),
                            start = Offset.Zero,
                            end = Offset.Infinite
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    authViewModel.register(username, email, password)
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8DAAEE)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ĐĂNG KÝ", fontSize = 25.sp, textAlign = TextAlign.Center)
            }

            Row(
                modifier = Modifier.padding(top = 5.dp)
            ) {
                Text("Bạn đã có tài khoản?", fontSize = 16.sp, color = Color.White)
                Text(
                    "Đăng Nhập",
                    fontSize = 18.sp,
                    color = Color.White,
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .clickable {
                            navController.navigate(Routes.LOGIN)
                        }
                )
            }
        }
        // Loading overlay khi đăng ký
        if (authState is AuthResult.Loading) {
            LoadingOverlay()
        }
    }
}
