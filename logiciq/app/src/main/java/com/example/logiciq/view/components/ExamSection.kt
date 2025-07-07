package com.example.logiciq.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.logiciq.navigation.Routes

@Composable
fun ExamSection(navController: NavController) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        Text("Bài Thi", fontWeight = FontWeight.Bold)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            items(3) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF3F6ABA)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .width(330.dp)
                        .clickable { navController.navigate(Routes.TEST)}
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
                            Text("Name bài thi", fontWeight = FontWeight.Bold, color = Color.White)
                            Text("3 câu hỏi", color = Color.White)
                            Row( modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp)
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                                Text(
                                    "name user",
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
}


