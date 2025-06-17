package com.example.logiciq.ui.screens

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.logiciq.R
import org.threeten.bp.LocalDate
import org.threeten.bp.DayOfWeek
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.text.font.Font
import com.example.logiciq.navigation.Routes


@Composable
fun HomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF84B6F4))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 64.dp, top = 30.dp) // dành chỗ cho BottomNavigation
        ) {
            item { TopBar() }
            item { CalendarRow() }
            item { ReminderSection() }
            item { SubjectSection() }
            item { ClassSection() }
        }

        BottomNavigationBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        var search by rememberSaveable { mutableStateOf("") }
        TextField(
            value = search,
            onValueChange = { search = it },
            placeholder = {
                Text("Search", color = Color.Gray)
            },
            modifier = Modifier
                .width(270.dp),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(30.dp)
                        .padding(start = 3.dp)
                )
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color(0xFFF2F2F2),
                unfocusedContainerColor = Color(0xFFF2F2F2),
                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray
            )
        )


        Icon(
            painter = painterResource(id = R.drawable.logic_iq),
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = Color.Unspecified
        )
    }
}

@Composable
fun CalendarRow() {
    val currentDate = remember { LocalDate.now() }
    val startOfWeek = remember { currentDate.with(DayOfWeek.MONDAY) }
    val days = remember(currentDate) {
        (0..6).map { offset -> startOfWeek.plusDays(offset.toLong()) }
    }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Lịch Hiện Hành", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(5.dp))
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            itemsIndexed(days) { _, date ->
                val isSelected = date == currentDate
                val dayNumber = date.dayOfMonth.toString()

                Column(
                    modifier = Modifier
                        .padding(horizontal = 6.dp, vertical = 8.dp)
                        .width(48.dp)
                        .height(88.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) Color(0xFFFFE6E6) else Color.Transparent),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = dayNumber,
                        color = if (isSelected) Color(0xFFEB3B5A) else Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    Text(
                        text = when (date.dayOfWeek.value) {
                            1 -> "Th 2"
                            2 -> "Th 3"
                            3 -> "Th 4"
                            4 -> "Th 5"
                            5 -> "Th 6"
                            6 -> "Th 7"
                            7 -> "CN"
                            else -> ""
                        },
                        color = if (isSelected) Color(0xFFEB3B5A) else Color(0xFF4A90E2),
                        fontSize = 14.sp
                    )
                    if (isSelected) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFFEB3B5A), shape = CircleShape)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReminderSection() {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        Text("Lời Nhắc Nhở", fontWeight = FontWeight.Bold)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF8572FF)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .height(75.dp)
                        .width(330.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(color = Color(0xFFBAB0F9), shape = RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(24.dp))
                        Column {
                            Text("DEMO GHI CHÚ LỊCH", fontWeight = FontWeight.Bold, color = Color.White)
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "12.00 - 16.00",
                                    color = Color.White
                                )
                            }

                        }
                    }
                }
            }
        }
        // Add form xữ lí tạo lịch với modal "CHỈ" lịch trong ngày (update lịch tự lựa chọn)
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDE496E)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.padding(top = 8.dp, start = 24.dp)
        ) {
            Text("Tạo Lịch", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun SubjectSection() {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        Text("Học Phần", fontWeight = FontWeight.Bold)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF3F6ABA)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .width(330.dp)
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
                            Text("Name học phần", fontWeight = FontWeight.Bold, color = Color.White)
                            Text("3 thuật ngữ", color = Color.White)
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

@Composable
fun ClassSection() {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        Text("Lớp Học", fontWeight = FontWeight.Bold)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF3F6ABA)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .width(330.dp)
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
                            Text("Name Lớp", fontWeight = FontWeight.Bold, color = Color.White)
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
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

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(color = Color(0xFFBDD0FF))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.AccountBox, contentDescription = null, tint = Color.Black)
                                        Spacer(Modifier.width(4.dp))
                                        Text("1 members", color = Color.Black, fontWeight = FontWeight.SemiBold)
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

@Composable
fun BottomNavigationBar(navController: NavController, modifier: Modifier = Modifier) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color(0xFF3F6ABA),
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(Routes.HOME) },
            icon = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFF6FA8DC), shape = CircleShape)
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(36.dp), tint = Color.White)
                }
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = { /* Navigate to Add screen */ },
            icon = { Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(36.dp), tint = Color.White) }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(Routes.LIBRARY)},
            icon = { Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(36.dp), tint = Color.White) }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(Routes.PROFILE) },
//            icon = {
//                Icon(
//                    painter = painterResource(id = R.drawable.logic_iq),
//                    contentDescription = "Icon minh họa cho tài khoản",
//                    modifier = Modifier.size(24.dp)
//                )
//            },
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(36.dp), tint = Color.White) }
        )
    }
}
