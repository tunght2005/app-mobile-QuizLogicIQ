package com.example.logiciq.navigation
// Nhóm: login, register, forgot...

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.logiciq.ui.screens.*

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    composable(Routes.WELCOME) { WelcomeScreen(navController) }
    composable(Routes.START) { StartScreen(navController) }
    composable(Routes.LOGIN) { LoginScreen(navController) }
    composable(Routes.REGISTER) { RegisterScreen(navController) }
    composable(Routes.RESET) { ResetPasswordScreen(navController) }
//    composable(Routes.VERIFY_CODE) { VerifyCodeScreen(navController) }
//    composable(Routes.CONFIRM) { ConfirmScreen(navController) }
}

