package com.example.logiciq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.logiciq.screens.HomeScreen
import com.example.logiciq.screens.LoginScreen
import com.example.logiciq.screens.RegisterScreen
import com.example.logiciq.screens.ResetPasswordScreen
import com.example.logiciq.screens.StartScreen
import com.example.logiciq.screens.WelcomeScreen

import com.example.logiciq.ui.theme.LogicIQTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LogicIQTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "welcome_screen") {
                        composable("welcome_screen") { WelcomeScreen(navController) }
                        composable("start_screen") { StartScreen(navController) }
                        composable("login_screen") { LoginScreen(navController) }
                        composable("reset_password_screen") { ResetPasswordScreen(navController) }
                        composable("register_screen") { RegisterScreen(navController) }
                        composable("home_screen") { HomeScreen(navController) }
                    }
                }
            }
        }
    }
}