package com.example.logiciq.navigation
// Nhóm: home, class...

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.logiciq.ui.screens.*

fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    composable(Routes.HOME) { HomeScreen(navController) }
//    composable(Routes.SUBJECT) { SubjectScreen(navController) }
//    composable(Routes.CLASS) { ClassScreen(navController) }
//    composable(Routes.QUIZ) { QuizScreen(navController) }
//    composable(Routes.QUIZ_DETAIL) { QuizDetailScreen(navController) }
//    composable(Routes.PROFILE) { ProfileScreen(navController) }
//    composable(Routes.SETTING) { SettingScreen(navController) }
//    composable(Routes.CHANGE_PASSWORD) { ChangePasswordScreen(navController) }
}
