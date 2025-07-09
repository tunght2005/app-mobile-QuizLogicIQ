package com.example.logiciq.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.logiciq.data.model.Quiz
import com.example.logiciq.navigation.Routes
import com.example.logiciq.viewmodel.QuizViewModel
@Composable
fun ExamSection(
    navController: NavController,
    testList: List<Quiz>,
    viewModel: QuizViewModel,
    onDeleteSuccess: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        Text("Bài Thi", fontWeight = FontWeight.Bold, color = Color.White)

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(testList) { quiz ->
                var showDialog by remember { mutableStateOf(false) }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF3F6ABA)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .width(330.dp)
                        .clickable {
                            navController.navigate(Routes.TEST)
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Icon ba chấm
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.TopEnd
                            ) {
                                IconButton(onClick = { showDialog = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "Tùy chọn",
                                        tint = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = quiz.title.ifEmpty { "Bài kiểm tra chưa có tiêu đề" },
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "${quiz.questions.size} câu hỏi",
                                color = Color.White
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp)
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                                Text(
                                    text = quiz.createdByName,
                                    color = Color.White,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }
                        }
                    }
                }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Tùy chọn bài thi") },
                        text = {
                            Column {
                                TextButton(onClick = {
                                    showDialog = false
                                    navController.navigate("${Routes.SHARE_TO_CLASS}/${quiz.id}")
                                }) {
                                    Icon(Icons.Default.Share, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Chia sẻ vào lớp")
                                }

                                TextButton(onClick = {
                                    showDialog = false
                                    viewModel.deleteQuiz(
                                        quizId = quiz.id,
                                        onSuccess = { onDeleteSuccess() },
                                        onFailure = { /* TODO: Hiển thị lỗi nếu cần */ }
                                    )
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Xoá bài thi")
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("Đóng")
                            }
                        }
                    )
                }

            }

        }
    }
}