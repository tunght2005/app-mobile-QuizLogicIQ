package com.example.logiciq.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.logiciq.view.components.ClassTabContent
import com.example.logiciq.view.components.TestTabContent
import com.example.logiciq.viewmodel.LibraryViewModel
import com.example.logiciq.viewmodel.TestViewModel

@Composable
fun LibraryScreen(navController: NavController) {
    val classViewModel: LibraryViewModel = viewModel()
    val testViewModel: TestViewModel = viewModel()

    val classList by classViewModel.classList.collectAsState(initial = emptyList())
    val testList by testViewModel.testList.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        classViewModel.loadClasses()
        testViewModel.loadTests() // 🆕 Tải danh sách bài thi từ Firestore
    }

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("BÀI THI", "LỚP HỌC")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E293B))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 58.dp, start = 30.dp, end = 30.dp, bottom = 40.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
            Text(
                text = "Thư Viện",
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = Color.Cyan,
                    height = 3.dp
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            color = if (selectedTab == index) Color.White else Color.LightGray
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 40.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (selectedTab == 0) {
                if (testList.isNotEmpty()) {
                    // 🆕 Hiển thị danh sách bài thi
                    testList.forEach { quiz ->
                        TestTabContent(
                            navController = navController,
                            quiz = quiz
                        )
                    }
                } else {
                    Text(
                        text = "Không có bài thi nào.",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            } else {
                ClassTabContent(
                    navController = navController,
                    classList = classList
                )
            }
        }
    }
}
