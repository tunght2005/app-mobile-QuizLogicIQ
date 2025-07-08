package com.example.logiciq.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
fun ClassSection(navController: NavController, classList: List<ClassItem>) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        Text("Lớp Học", fontWeight = FontWeight.Bold, color = Color.White)

        if (classList.isEmpty()) {
            Text(
                text = "Chưa có lớp học nào.",
                color = Color.White,
                modifier = Modifier.padding(top = 12.dp)
            )
        } else {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ✅ Dùng đúng items từ Compose
                items(classList) { classItem ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF3F6ABA)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .width(330.dp)
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
                                Text(
                                    text = classItem.name,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    // Số lượng học phần (hiện mặc định là 1)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(color = Color(0xFFBDD0FF))
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Menu, contentDescription = null, tint = Color.Black)
                                            Spacer(Modifier.width(4.dp))
                                            Text("1 học phần", color = Color.Black, fontWeight = FontWeight.SemiBold)
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
                                            Text(
                                                text = "${classItem.memberCount} members",
                                                color = Color.Black,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
