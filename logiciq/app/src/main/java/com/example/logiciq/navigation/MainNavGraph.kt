package com.example.logiciq.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.logiciq.view.screens.*

fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    composable(Routes.HOME) { HomeScreen(navController) }
    composable(Routes.LIBRARY) { LibraryScreen(navController) }
    composable(Routes.CLASS) { ClassScreen(navController) }

    composable(Routes.NEWCLASS) {
        NewClassScreen(
            navController = navController,
            onBack = { navController.popBackStack() },
            onSave = {
                // logic lưu lớp học nếu có
                navController.popBackStack()
            }
        )
    }

    composable(Routes.NEWSUBJECT) {
        NewSubjectScreen(
            navController = navController,
            onBack = { navController.popBackStack() },
            onSave = {
                // TODO: thực hiện lưu học phần ở đây nếu có logic
                navController.popBackStack()
            }
        )
    }

    composable(Routes.NEWTEST) {
        NewTestScreen(
            navController = navController,
            onBack = { navController.popBackStack() },
            onSave = {
                // TODO: thực hiện lưu đề thi ở đây nếu có logic
                navController.popBackStack()
            }
        )
    }

    composable(Routes.LEARNING) { LearningScreen(navController) }
    composable(Routes.SUBJECT) { SubjectScreen(navController) }
    composable(Routes.TEST) { TestScreen(navController) }
    composable(Routes.EXAM) { ExamScreen(navController) }
    composable(Routes.PROFILE) { ProfileScreen(navController) }
    composable(Routes.HISTORY) { HistoryScreen(navController) }

    composable(Routes.SETTING) {
        SettingsScreen(
            navController = navController,
            onLogoutClick = {
                navController.navigate("auth") {
                    popUpTo(0) { inclusive = true } // Xóa toàn bộ backstack
                }
            }
        )
    }

    // composable(Routes.CHANGE_PASSWORD) { ChangePasswordScreen(navController) }
}
