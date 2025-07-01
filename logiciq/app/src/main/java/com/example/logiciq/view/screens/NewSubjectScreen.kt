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
fun NewSubjectScreen(
    navController: NavController,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var itemList by remember { mutableStateOf(listOf(FlashcardItem("", ""))) }

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
                text = "Tạo học phần",
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
                // Tiêu đề
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
                // Nút thêm flashcard
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${itemList.size}/${itemList.size}",
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedButton(
                        onClick = {
                            itemList = itemList + FlashcardItem("", "")
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

            items(itemList.size) { index ->
                val item = itemList[index]
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
                    Box(){
                        IconButton(
                            onClick = {
                                itemList = itemList.filterIndexed { i, _ -> i != index }
                            },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Xoá",
                                tint = Color.White
                            )
                        }
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Trường Thuật ngữ
                            TextField(
                                value = item.term,
                                onValueChange = { term ->
                                    itemList = itemList.mapIndexed { i, current ->
                                        if (i == index) current.copy(term = term) else current
                                    }
                                },
                                placeholder = { Text("Nhập từ", color = Color.LightGray) },
                                singleLine = true,
                                colors = textFieldColors(),
                                modifier = Modifier.fillMaxWidth()
                                    .padding(top = 10.dp)
                            )
                            Text(
                                text = "THUẬT NGỮ",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Trường Định nghĩa
                            TextField(
                                value = item.definition,
                                onValueChange = { def ->
                                    itemList = itemList.mapIndexed { i, current ->
                                        if (i == index) current.copy(definition = def) else current
                                    }
                                },
                                placeholder = { Text("Nhập nghĩa", color = Color.LightGray) },
                                singleLine = true,
                                colors = textFieldColors(),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = "ĐỊNH NGHĨA",
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

// Màu text field
@Composable
fun textFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
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
}

// Flashcard data class
data class FlashcardItem(
    val term: String,
    val definition: String
)
