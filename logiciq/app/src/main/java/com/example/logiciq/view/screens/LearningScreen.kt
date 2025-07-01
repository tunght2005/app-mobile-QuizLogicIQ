package com.example.logiciq.view.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.logiciq.R
import kotlinx.coroutines.delay

// Data model for question
data class Question(val term: String, val options: List<String>, val correctAnswer: String)

@Composable
fun LearningScreen(navController: NavController) {
    val questionList = listOf(
        Question("NAME THUẬT NGỮ 1", listOf("DEMO ITEM 1", "DEMO ITEM 2"), "DEMO ITEM 1"),
        Question("NAME THUẬT NGỮ 2", listOf("DEMO ITEM A", "DEMO ITEM B"), "DEMO ITEM B"),
        Question("NAME THUẬT NGỮ 3", listOf("ABC", "XYZ"), "XYZ"),
        Question("NAME THUẬT NGỮ 4", listOf("Option 1", "Option 2"), "Option 1")
    )

    var correctCount by remember { mutableStateOf(0) }
    var wrongCount by remember { mutableStateOf(0) }
    var questionIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }

    val currentQuestion = questionList.getOrNull(questionIndex)

    LaunchedEffect(selectedAnswer) {
        if (selectedAnswer.isNotEmpty()) {
            delay(500) // UX cho câu hỏi
            if (selectedAnswer == currentQuestion?.correctAnswer) correctCount++ else wrongCount++
            selectedAnswer = ""
            if (questionIndex < questionList.lastIndex) questionIndex++ else showResult = true
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF1E293B)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Top Bar
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 58.dp, start = 24.dp, end = 24.dp, bottom = 10.dp)
            ) {
                IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.CenterStart)) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(40.dp))
                }
                if (!showResult) {
                    Row(modifier = Modifier.align(Alignment.Center), verticalAlignment = Alignment.CenterVertically) {
                        Icon(painterResource(R.drawable.check), contentDescription = null, tint = Color.Green, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("$correctCount", fontSize = 28.sp, color = Color.White)
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(painterResource(R.drawable.cancel), contentDescription = null, tint = Color.Red, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("$wrongCount", fontSize = 28.sp, color = Color.White)
                    }
                } else {
                    Text("Tổng Số", modifier = Modifier.align(Alignment.Center), color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Medium)
                }
            }

            if (showResult) {
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(painterResource(R.drawable.check), contentDescription = null, tint = Color.Green, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("$correctCount", fontSize = 28.sp, color = Color.White)
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(painterResource(R.drawable.cancel), contentDescription = null, tint = Color.Red, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("$wrongCount", fontSize = 28.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("HÃY HỌC NHIỀU\nĐỂ CÓ KẾT QUẢ TỐT NHÉ!", color = Color.Green, fontSize = 20.sp, textAlign = TextAlign.Center)
                }
            } else {
                Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFFB6EAFF), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(currentQuestion?.term ?: "", color = Color(0xFF0A1D42), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Chọn câu trả lời", color = Color.LightGray, modifier = Modifier.align(Alignment.Start))
                    Spacer(modifier = Modifier.height(16.dp))

                    currentQuestion?.options?.forEach { answer ->
                        Surface(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                .border(
                                    BorderStroke(3.dp, when {
                                        selectedAnswer == answer && answer == currentQuestion.correctAnswer -> Color.Green
                                        selectedAnswer == answer && answer != currentQuestion.correctAnswer -> Color.Red
                                        else -> Color.White
                                    }),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable(enabled = selectedAnswer.isEmpty()) { selectedAnswer = answer },
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF1A2A55),
                            tonalElevation = 2.dp,
                            shadowElevation = 4.dp
                        ) {
                            Text(answer, modifier = Modifier.padding(16.dp), color = Color.White, fontSize = 16.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Câu hỏi ${questionIndex + 1}/${questionList.size}",
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.logic_iq),
            contentDescription = "Logo",
            modifier = Modifier.size(400.dp).align(Alignment.CenterHorizontally).padding(bottom = 100.dp)
        )
    }
}
