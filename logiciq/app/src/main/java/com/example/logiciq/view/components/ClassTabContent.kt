package com.example.logiciq.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.logiciq.data.model.ClassItem

@Composable
fun ClassTabContent(
    navController: NavController,
    classList: List<ClassItem>
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(classList) { classItem ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF3F6ABA)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("class_screen/${classItem.id}")
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(classItem.name, fontWeight = FontWeight.Bold, color = Color.White)

                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // Số lượng bài thi (giả lập 1)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(color = Color(0xFFBDD0FF))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Menu, contentDescription = null, tint = Color.Black)
                                    Spacer(Modifier.width(4.dp))
                                    Text("1 bai thi", color = Color.Black, fontWeight = FontWeight.SemiBold)
                                }
                            }

                            Spacer(Modifier.width(25.dp))

                            // Số lượng thành viên
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(color = Color(0xFFBDD0FF))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AccountBox, contentDescription = null, tint = Color.Black)
                                    Spacer(Modifier.width(4.dp))
                                    Text("${classItem.memberCount} members", color = Color.Black, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
