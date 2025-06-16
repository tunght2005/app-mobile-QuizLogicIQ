package com.example.logiciq.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.logiciq.R
import com.example.logiciq.navigation.Routes
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ProfileScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A1D42)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 58.dp, start = 30.dp, end = 30.dp, bottom = 40.dp)
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
                    text = "Hồ Sơ",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Image(
                painter = painterResource(R.drawable.avatar),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(115.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Text("Name",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
            )

            Button(
                onClick = { navController.navigate(Routes.SETTING) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F6ABA)),
                modifier = Modifier
                    .width(330.dp)
                    .height(70.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color.White)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Cài đặt",
                        tint = Color.White,
                        modifier = Modifier
                            .size(74.dp)
                            .padding(end = 12.dp)
                    )
                    Text(
                        text = "Cài Đặt Của Bạn",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { /* TODO: Lịch sử hoạt động */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F6ABA)),
                modifier = Modifier
                    .width(330.dp)
                    .height(70.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color.White)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Cài đặt",
                        tint = Color.White,
                        modifier = Modifier
                            .size(74.dp)
                            .padding(end = 12.dp)
                    )
                    Text(
                        text = "Lịch Sử Hoạt Động",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
            }
        }

        Image(
            painter = painterResource(R.drawable.logic_iq),
            contentDescription = "Logo",
            modifier = Modifier
                .size(400.dp)
                .padding(bottom = 100.dp)
        )
    }
}
