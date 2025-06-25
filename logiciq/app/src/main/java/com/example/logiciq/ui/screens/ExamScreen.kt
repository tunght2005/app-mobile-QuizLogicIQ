package com.example.logiciq.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
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
// Tái sử dụng ở Learning rồi: data class Question(val term: String, val options: List<String>, val correctAnswer: String)

data class UserScore(val name: String, val correct: Int, val wrong: Int)

@Composable
fun ExamScreen(navController: NavController) {
    val questionList = listOf(
        Question("CÂU HỎI 1", listOf("DAP AN 1", "DAP AN 2", "DAP AN 3", "DAP AN 4"), "DAP AN 1"),
        Question("CÂU HỎI 2", listOf("DAP AN A", "DAP AN B", "DAP AN C", "DAP AN D"), "DAP AN B"),
        Question("CÂU HỎI 3", listOf("DAP AN 1", "DAP AN 2", "DAP AN 3", "DAP AN 4"), "DAP AN 3"),
        Question("CÂU HỎI 4", listOf("DAP AN 1", "DAP AN 2", "DAP AN 3", "DAP AN 4"), "DAP AN 2")
    )

    val leaderboard = listOf(
        UserScore("USER 1", 4, 0),
        UserScore("USER 2", 3, 1),
        UserScore("USER 3", 2, 2),
        UserScore("USER 4", 1, 3)
    )

    var correctCount by remember { mutableStateOf(0) }
    var wrongCount by remember { mutableStateOf(0) }
    var questionIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }

    val currentQuestion = questionList.getOrNull(questionIndex)

    LaunchedEffect(selectedAnswer) {
        if (selectedAnswer.isNotEmpty()) {
            delay(500)
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
                    Text("Bảng Xếp Hạng", modifier = Modifier.align(Alignment.Center), color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Medium)
                }
            }

            if (showResult) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 24.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        itemsIndexed(leaderboard) { index, user ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .background(
                                        color = when (index) {
                                            0 -> Color(0xFF3B82F6)
                                            1 -> Color(0xFF60A5FA)
                                            2 -> Color(0xFF93C5FD)
                                            else -> Color(0xFF3F6ABA)
                                        },
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = listOf("🏆", "🥈", "🥉").getOrNull(index) ?: "${index + 1}",
                                    fontSize = 20.sp,
                                    color = Color.Yellow,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.avatar),
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(user.name, fontSize = 20.sp, color = Color.White, modifier = Modifier.weight(1f))
                                Icon(painterResource(R.drawable.check), contentDescription = null, tint = Color.Green, modifier = Modifier.size(28.dp))
                                Text("${user.correct}", fontSize = 20.sp, color = Color.White, modifier = Modifier.padding(start = 8.dp))
                                Icon(painterResource(R.drawable.cancel), contentDescription = null, tint = Color.Red, modifier = Modifier.size(28.dp).padding(start = 8.dp))
                                Text("${user.wrong}", fontSize = 20.sp, color = Color.White, modifier = Modifier.padding(start = 4.dp))
                            }
                        }
                    }
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

                    currentQuestion?.options?.chunked(2)?.forEach { rowOptions ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            rowOptions.forEach { answer ->
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(vertical = 10.dp)
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
                                    Text(
                                        text = answer,
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth(),
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
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
