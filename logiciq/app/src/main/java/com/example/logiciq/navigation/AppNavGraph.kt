package com.example.logiciq.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.example.logiciq.navigation.Routes

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.AUTH // 👈 sử dụng route gốc "AUTH"
    ) {
        // Graph cho phần chưa đăng nhập
        navigation(
            startDestination = Routes.WELCOME,
            route = Routes.AUTH
        ) {
            authGraph(navController)
        }

        // Graph cho phần sau khi đã đăng nhập
        navigation(
            startDestination = Routes.HOME,
            route = Routes.MAIN
        ) {
            mainGraph(navController)
        }
    }
}
