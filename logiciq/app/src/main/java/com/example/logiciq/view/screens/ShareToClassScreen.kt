@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.logiciq.view.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chọn lớp để chia sẻ") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        AllClassList(
            modifier = Modifier.padding(padding),
            onClassClick = { selectedClass ->
                isLoading = true
                viewModel.shareQuizToClass(
                    quizId = quizId,
                    classId = selectedClass.id,
                    onSuccess = {
                        isLoading = false
                        // Đánh dấu để ClassScreen reload quiz
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
