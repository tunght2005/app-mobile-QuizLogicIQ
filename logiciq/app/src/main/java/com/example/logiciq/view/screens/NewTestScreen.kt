@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.logiciq.view.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun NewTestScreen(
    navController: NavController,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var questionList by remember { mutableStateOf(listOf(QuestionItem("", List(4) { "" }))) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E293B))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 58.dp, start = 30.dp, end = 30.dp, bottom = 30.dp)
        ) {
            IconButton(
                onClick = { onBack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
            Text(
                text = "Tạo bài thi",
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
                fontSize = 25.sp
            )
            IconButton(
                onClick = { onSave() },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                // Tiêu đề bài thi
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = {
                        Text("Tiêu đề", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(bottom = 10.dp))
                    },
                    placeholder = {
                        Text("Chủ đề, chương, đơn vị...", color = Color.LightGray, fontSize = 18.sp)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        disabledTextColor = Color.Gray,

                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,

                        cursorColor = Color.White,

                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.LightGray,
                        disabledIndicatorColor = Color.Gray,

                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.LightGray
                    )
                )
            }

            item {
                // Nút thêm câu hỏi
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${questionList.size}/${questionList.size}",
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedButton(
                        onClick = {
                            questionList = questionList + QuestionItem("", List(4) { "" })
                        },
                        shape = CircleShape,
                        border = BorderStroke(3.dp, Color.White),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                    }
                }
            }

            items(questionList.size) { index ->
                val item = questionList[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF3F6ABA))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        // Xoá câu hỏi
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Câu hỏi ${index + 1}",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(
                                onClick = {
                                    questionList = questionList.filterIndexed { i, _ -> i != index }
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Xoá",
                                    tint = Color.White
                                )
                            }
                        }

                            // Trường Câu hỏi
                        TextField(
                            value = item.question,
                            onValueChange = { newQuestion ->
                                questionList = questionList.mapIndexed { i, q ->
                                    if (i == index) q.copy(question = newQuestion) else q
                                } },
                            placeholder = { Text("Nhập câu hỏi", color = Color.LightGray, fontSize = 20.sp) },
                            singleLine = true,
                            colors = textFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                                .padding(top = 10.dp)
                            )
                        Text(
                            text = "CÂU HỎI",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // 4 Trường Đáp án
                        val answerLabels = listOf("A", "B", "C", "D")
                        item.answers.forEachIndexed { i, answer ->
                            TextField(
                                value = answer,
                                onValueChange = { newAns ->
                                    questionList = questionList.mapIndexed { idx, q ->
                                        if (idx == index) {
                                            q.copy(answers = q.answers.toMutableList().apply { set(i, newAns) })
                                        } else q
                                    } },
                                placeholder = { Text("Nhập đáp án ${answerLabels[i]}", color = Color.LightGray) },
                                singleLine = true,
                                colors = textFieldColors(),
                                modifier = Modifier.fillMaxWidth()
                                        .padding(top = 8.dp)
                                )
                            Text(
                                text = "ĐÁP ÁN ${answerLabels[i]}",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Data model mới cho câu hỏi
data class QuestionItem(
    val question: String,
    val answers: List<String>
)
