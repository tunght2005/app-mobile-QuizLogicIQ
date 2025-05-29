package com.example.logiciq.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.logiciq.R

@Composable
fun StartScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF3F6ABA)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logic_iq),
            contentDescription = "Logic IQ Logo",
            modifier = Modifier.size(420.dp)
        )
        Text(
            "Bạn đã sẵn sàng để rèn luyện trí tuệ chưa?",
            style = TextStyle(
                fontSize = 32.sp,
                lineHeight = 50.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .offset(x = 0.dp, y = (-100).dp)
                .padding(30.dp)
        )
        Spacer(modifier = Modifier.height(80.dp))
        Button(onClick = { navController.navigate("login_screen")},
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .width(200.dp)
                .height(60.dp)
                .border(2.dp, Color.Black, RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.White,
            )
        ) {
            Text("BẮT ĐẦU", fontSize = 32.sp, color = Color.Black, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}