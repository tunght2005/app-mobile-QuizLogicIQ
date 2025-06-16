package com.example.logiciq.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.example.logiciq.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.logiciq.navigation.Routes

@Composable
fun LoginScreen(navController: NavController) {
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
        Text("Đăng nhập để tham gia học tập!",
            style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 50.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .offset(x = 0.dp, y = (-100).dp)
                .padding(start = 30.dp, end = 30.dp, top = 30.dp)
        )

        var username by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        var passwordVisibility by remember { mutableStateOf(false)}

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
//            label = { Text("Tên đăng nhập") },
            placeholder = { Text("Nhập tên đăng nhập", color = Color.White) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User Icon",
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
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Mật khẩu", color = Color.White) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password Icon",
                    tint = Color.White,
                    modifier = Modifier.size(35.dp)
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        painter = painterResource(id = if (passwordVisibility) R.drawable.visibility else R.drawable.visibilityoff),
                        contentDescription = if (passwordVisibility) "Hide password" else "Show password",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
        Text(
            "Quên mật khẩu?",
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
            navController.navigate(Routes.RESET)}
                .padding(8.dp),
            textAlign = TextAlign.End,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Xử lý đăng nhập */navController.navigate(Routes.HOME) },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF8DAAEE)
            )
            ) {
            Text("ĐĂNG NHẬP",
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
        Spacer(modifier = Modifier.height(16.dp))
        Text("OR", fontSize = 20.sp, color = Color.White, textDecoration = TextDecoration.Underline)
        Button(onClick = { /*Đăng nhập google*/},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        )
        {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "logo google",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(999.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}
