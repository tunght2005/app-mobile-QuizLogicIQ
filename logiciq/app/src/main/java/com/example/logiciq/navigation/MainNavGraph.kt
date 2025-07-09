package com.example.logiciq.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.logiciq.view.screens.*
import com.example.logiciq.viewmodel.ClassViewModel
import com.google.firebase.auth.FirebaseAuth

fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    composable(Routes.HOME) {
        HomeScreen(navController)
    }

    composable(Routes.LIBRARY) {
        LibraryScreen(navController)
    }

    composable("${Routes.CLASS}/{classId}") { backStackEntry ->
        val classId = backStackEntry.arguments?.getString("classId") ?: ""
        ClassScreen(navController = navController, classId = classId)
    }

    composable(Routes.NEWCLASS) {
        val viewModel: ClassViewModel = viewModel()
        val user = FirebaseAuth.getInstance().currentUser

        NewClassScreen(
            navController = navController,
            onBack = { navController.popBackStack() },
            onSave = { name, description ->
                if (user != null) {
                    viewModel.createClass(name, description) { result ->
                        if (result.isSuccess) {
                            navController.navigate(Routes.LIBRARY) {
                                popUpTo(Routes.HOME) { inclusive = false }
                            }
                        } else {
                            println("❌ Lỗi tạo lớp: ${result.exceptionOrNull()?.message}")
                        }
                    }
                }
            }
        )
    }

    composable(Routes.NEWTEST) {
        NewTestScreen(
            navController = navController,
            onBack = { navController.popBackStack() },
            onSave = {
                navController.navigate(Routes.LIBRARY) {
                    popUpTo(Routes.HOME) { inclusive = false }
                }
            }
        )
    }

    composable(Routes.TEST) {
        TestScreen(navController)
    }

    composable(Routes.EXAM) {
        ExamScreen(navController)
    }

    composable(Routes.PROFILE) {
        ProfileScreen(navController)
    }

    composable(Routes.HISTORY) {
        HistoryScreen(navController)
    }

    composable(Routes.SETTING) {
        SettingsScreen(
            navController = navController,
            onLogoutClick = {
                navController.navigate("auth") {
                    popUpTo(0) { inclusive = true }
                }
            }
        )
    }

    // ✅ Thêm dòng này để mở màn hình chia sẻ bài thi vào lớp
    composable("${Routes.SHARE_TO_CLASS}/{quizId}") { backStackEntry ->
        val quizId = backStackEntry.arguments?.getString("quizId") ?: ""
        ShareToClassScreen(navController, quizId)
    }
}

