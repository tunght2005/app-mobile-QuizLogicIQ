@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.logiciq.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.logiciq.data.model.Quiz
import com.example.logiciq.navigation.Routes
import com.example.logiciq.viewmodel.QuizViewModel

@Composable
fun ExamSection(
    navController: NavController,
    testList: List<Quiz>,
    quizId: String,
    viewModel: QuizViewModel,
    onDeleteSuccess: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        Text("Bài Thi", fontWeight = FontWeight.W600,
            fontSize = 20.sp)

        if (testList.isEmpty()) {
            Text(
                text = "Chưa có bài thi nào",
                color = Color.Red,
                fontWeight = FontWeight.W300,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 60.dp, end = 60.dp, top = 20.dp)
            )
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(testList) { quiz ->
                var showDialog by remember { mutableStateOf(false) }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF3F6ABA)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .width(330.dp)
                        .clickable {
                            navController.navigate("test_screen/${quiz.id}")
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
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                            ) {

                                Text(
                                    text = quiz.title.ifEmpty { "Bài kiểm tra chưa có tiêu đề" },
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier
                                        .widthIn(max = 200.dp)
                                        .align(Alignment.Center),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                IconButton(
                                    onClick = { showDialog = true },
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "Tùy chọn",
                                        tint = Color.White
                                    )
                                }
                            }

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
                                    imageVector = Icons.Default.Person,
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
                        containerColor = Color(0xFF1B263B),
                        titleContentColor = Color.White,
                        textContentColor = Color.White,
                        title = { Text("Tùy chọn bài thi", fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()) },
                        text = {
                            Column( modifier = Modifier
                                .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally) {
                                TextButton(onClick = {
                                    showDialog = false
                                    navController.navigate("${Routes.SHARE}/${quiz.id}")
                                }) {
                                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text("Chia Sẽ Vào Lớp")
                                }

                                TextButton(onClick = {
                                    showDialog = false
                                    viewModel.deleteQuiz(
                                        quizId = quiz.id,
                                        onSuccess = { onDeleteSuccess() },
                                        onFailure = { }
                                    )
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text("Xoá Bài Thi")
                                }
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = { showDialog = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8DAAEE))
                            ) {
                                Text(
                                    text = "Đóng",
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }



                        }
                    )
                }
            }
        }
    }
}
