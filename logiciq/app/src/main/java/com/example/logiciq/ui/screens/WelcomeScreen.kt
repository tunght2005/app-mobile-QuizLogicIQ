package com.example.logiciq.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.example.logiciq.R
import com.example.logiciq.navigation.Routes

@Composable
fun WelcomeScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(1000L)
        navController.navigate(Routes.START) {
            popUpTo(Routes.WELCOME) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF3F6ABA))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logic_iq),
                contentDescription = "Quizz Logic IQ Logo",
                modifier = Modifier.size(420.dp)
            )
//            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                color = androidx.compose.ui.graphics.Color.White,
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .offset(x = 0.dp, y = (-60).dp)
                    .height(8.dp)
            )
        }
    }
}