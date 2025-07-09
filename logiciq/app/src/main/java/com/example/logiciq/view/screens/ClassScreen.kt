@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.logiciq.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.logiciq.view.components.MemberTabContent
import com.example.logiciq.view.components.TestTabContent
import com.example.logiciq.viewmodel.ClassViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun ClassScreen(
    navController: NavController,
    classId: String
) {
    val viewModel: ClassViewModel = viewModel()
    val classState by viewModel.classState.collectAsState()
    val quizList by viewModel.quizList.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    // Lần đầu load class và quizzes
    LaunchedEffect(classId) {
        viewModel.loadClass(classId)
        viewModel.loadQuizzes(classId)
    }

    // Nếu có quiz mới được chia sẻ vào lớp thì reload lại danh sách quiz
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    LaunchedEffect(savedStateHandle?.get<Boolean>("quizShared")) {
        if (savedStateHandle?.get<Boolean>("quizShared") == true) {
            viewModel.loadQuizzes(classId)
            savedStateHandle["quizShared"] = false
        }
    }

    if (classState == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
        return
    }

    val classItem = classState!!
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("BÀI THI", "THÀNH VIÊN")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E293B))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 58.dp, start = 20.dp, end = 20.dp, bottom = 30.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Text(
                text = classItem.name,
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )

            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Tuỳ chọn",
                    tint = Color.White
                )
            }
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
                .padding(horizontal = 20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    if (quizList.isNotEmpty()) {
                        TestTabContent(navController = navController, testList = quizList)
                    } else {
                        Text(
                            text = "Chưa có bài thi nào.",
                            color = Color.White,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
                1 -> MemberTabContent(members = classItem.members)
            }
        }
    }

    // Dialog xác nhận xoá lớp học
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Tuỳ chọn lớp học") },
            text = {
                Text("Bạn có chắc chắn muốn xoá lớp học này? Thao tác này không thể hoàn tác.")
            },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    viewModel.deleteClass(classId) {
                        navController.popBackStack() // Quay lại sau khi xoá
                    }
                }) {
                    Text("Xoá", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Huỷ")
                }
            }
        )
    }
}
