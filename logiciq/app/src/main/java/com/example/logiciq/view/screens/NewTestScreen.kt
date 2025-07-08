@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.logiciq.view.screens

import android.util.Log
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.logiciq.data.model.Question
import com.example.logiciq.data.model.Quiz
import com.example.logiciq.viewmodel.QuizViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NewTestScreen(
    navController: NavController,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    val viewModel: QuizViewModel = viewModel()
    var title by remember { mutableStateOf("") }
    var questionList by remember { mutableStateOf(listOf(QuestionItem("", List(4) { "" }))) }

    fun saveTest() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Log.e("NewTestScreen", "⚠️ Người dùng chưa đăng nhập.")
            return
        }

        val questions = questionList.mapIndexed { _, q ->
            Question.Type4(
                text = q.question,
                optionA = q.answers.getOrElse(0) { "" },
                optionB = q.answers.getOrElse(1) { "" },
                optionC = q.answers.getOrElse(2) { "" },
                optionD = q.answers.getOrElse(3) { "" },
                correctOption = 'A' // Có thể cho phép chọn trong UI sau
            )
        }

        val quiz = Quiz(
            title = title,
            questions = questions,
            maxDurationSeconds = 600,
            createBy = user.uid,
            createByName = user.displayName ?: user.email ?: "Không tên",
            createdAt = Timestamp.now()
        )

        viewModel.createQuiz(quiz) { success, error ->
            if (success) {
                onSave()
            } else {
                Log.e("NewTestScreen", "❌ Lỗi tạo quiz: $error")
            }
        }
    }

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
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(48.dp))
            }
            Text("Tạo bài thi", modifier = Modifier.align(Alignment.Center), color = Color.White, fontSize = 25.sp)
            IconButton(onClick = { saveTest() }, modifier = Modifier.align(Alignment.CenterEnd)) {
                Icon(Icons.Default.Check, contentDescription = "Save", tint = Color.White, modifier = Modifier.size(48.dp))
            }
        }

        LazyColumn(
            modifier = Modifier.padding(horizontal = 30.dp).weight(1f),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Tiêu đề", color = Color.White, fontSize = 18.sp) },
                    placeholder = { Text("Chủ đề, chương, đơn vị...", color = Color.LightGray, fontSize = 18.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = textFieldColors()
                )
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("${questionList.size} câu hỏi", color = Color.White, modifier = Modifier.weight(1f))
                    OutlinedButton(
                        onClick = { questionList = questionList + QuestionItem("", List(4) { "" }) },
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
                        .border(2.dp, Color.White, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF3F6ABA))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Câu hỏi ${index + 1}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            IconButton(
                                onClick = {
                                    questionList = questionList.filterIndexed { i, _ -> i != index }
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Xoá", tint = Color.White)
                            }
                        }

                        TextField(
                            value = item.question,
                            onValueChange = { newQuestion ->
                                questionList = questionList.mapIndexed { i, q ->
                                    if (i == index) q.copy(question = newQuestion) else q
                                }
                            },
                            placeholder = { Text("Nhập câu hỏi", color = Color.LightGray, fontSize = 20.sp) },
                            singleLine = true,
                            colors = textFieldColors(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        )

                        val answerLabels = listOf("A", "B", "C", "D")
                        item.answers.forEachIndexed { i, answer ->
                            TextField(
                                value = answer,
                                onValueChange = { newAns ->
                                    questionList = questionList.mapIndexed { idx, q ->
                                        if (idx == index) {
                                            q.copy(answers = q.answers.toMutableList().apply { set(i, newAns) })
                                        } else q
                                    }
                                },
                                placeholder = { Text("Đáp án ${answerLabels[i]}", color = Color.LightGray) },
                                singleLine = true,
                                colors = textFieldColors(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Model tạm cho UI
data class QuestionItem(val question: String, val answers: List<String>)

@Composable
fun textFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color.White,
    unfocusedBorderColor = Color.LightGray,
    focusedLabelColor = Color.White,
    unfocusedLabelColor = Color.LightGray,
    cursorColor = Color.White,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    disabledTextColor = Color.Gray,
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent
)
