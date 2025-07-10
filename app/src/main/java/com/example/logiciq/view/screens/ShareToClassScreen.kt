@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.logiciq.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.logiciq.view.components.AllClassList
import com.example.logiciq.viewmodel.ClassViewModel

@Composable
fun ShareToClassScreen(
    navController: NavController,
    quizId: String,
    viewModel: ClassViewModel = viewModel()
) {
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
            Text(
                text = "Chọn Lớp Chia Sẽ",
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium
            )
        }
        AllClassList(
            onClassClick = { selectedClass ->
                isLoading = true
                viewModel.shareQuizToClass(
                    quizId = quizId,
                    classId = selectedClass.id,
                    onSuccess = {
                        isLoading = false
                        navController.previousBackStackEntry
                            ?.savedStateHandle?.set("quizShared", true)
                        navController.popBackStack()
                    },
                    onFailure = {
                        isLoading = false
                        errorMessage = it.message
                    }
                )
            }
        )

        if (isLoading) {
            LinearProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        errorMessage?.let {
            SnackbarHost(hostState = remember { SnackbarHostState() }) {
                Snackbar {
                    Text("Lỗi: $it")
                }
            }
        }
    }
}
