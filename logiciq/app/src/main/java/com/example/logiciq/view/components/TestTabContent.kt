package com.example.logiciq.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.logiciq.data.model.Quiz
import com.example.logiciq.navigation.Routes

@Composable
fun TestTabContent(
    navController: NavController,
    quiz: Quiz
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF3F6ABA)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(Routes.TEST)
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                            Text(
                                text = quiz.createByName,
                                color = Color.White,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
