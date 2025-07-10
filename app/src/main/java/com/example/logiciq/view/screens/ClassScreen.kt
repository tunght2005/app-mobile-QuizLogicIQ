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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun ClassScreen(
    navController: NavController,
    classId: String
) {
    val viewModel: ClassViewModel = viewModel()
    val classState by viewModel.classState.collectAsState()
    val quizList by viewModel.quizList.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFF1E293B)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, start = 20.dp, end = 20.dp, bottom = 30.dp)
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
                    modifier = Modifier
                        .align(Alignment.Center)
                        .widthIn(max = 200.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
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

            Spacer(modifier = Modifier.height(50.dp))

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
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

        //Xử lí menu tham gia, rời lớp, xóa lớp.
        if (showDialog) {
            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
            val isCreator = currentUserId != null && classItem.createdBy == currentUserId
            val isMember = classItem.members.any { it.userId == currentUserId }
            val canLeave = isMember && !isCreator

            AlertDialog(
                onDismissRequest = { showDialog = false },
                containerColor = Color(0xFF1B263B),
                titleContentColor = Color.White,
                textContentColor = Color.White,
                title = { Text("Tuỳ chọn lớp học", fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()) },
                text = {
                    Text("Bạn muốn làm gì với lớp học này?", fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center, fontSize = 16.sp,
                        modifier = Modifier.fillMaxWidth())
                },
                confirmButton = {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (isCreator) {
                            TextButton(
                                onClick = {
                                    showDialog = false
                                    viewModel.deleteClass(classId) {
                                        navController.popBackStack()
                                    }
                                }
                            ) {
                                Text("Xoá", color = Color.Red)
                            }
                        }

                        if (canLeave) {
                            Button(
                                onClick = {
                                    showDialog = false
                                    viewModel.leaveClass(classId) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Đã rời khỏi lớp")
                                        }
                                        navController.popBackStack()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                            ) {
                                Text(
                                    text = "Rời lớp",
                                    color = Color.Red,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        if (!isMember) {
                            Button(
                                onClick = {
                                    showDialog = false
                                    viewModel.joinClass(classId) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Đã tham gia lớp thành công")
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                            ) {
                                Text(
                                    text = "Tham gia",
                                    color = Color(0xFF1B263B),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog = false },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text(
                            text = "Huỷ",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            )
        }
    }
}
