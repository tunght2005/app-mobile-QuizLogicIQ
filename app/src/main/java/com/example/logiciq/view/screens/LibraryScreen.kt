@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.logiciq.view.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.logiciq.view.components.ClassTabContent
import com.example.logiciq.view.components.TestTabContent
import com.example.logiciq.viewmodel.LibraryViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LibraryScreen(navController: NavController) {
    val viewModel: LibraryViewModel = viewModel()
    val classList by viewModel.classList.collectAsState()
    val testList by viewModel.testList.collectAsState()

    val userId = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            viewModel.loadClasses(userId)
            viewModel.loadTests(userId)
        } else {
            Log.e("LibraryScreen", "❌ userId is null. Chưa đăng nhập?")
        }
    }


    if (testList.isEmpty() && classList.isEmpty()) {
        CircularProgressIndicator()
    }


    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("BÀI THI", "LỚP HỌC")

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
                text = "Thư Viện",
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
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
                .padding(horizontal = 5.dp, vertical = 40.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (selectedTab == 0) {
                TestTabContent(
                    navController = navController,
                    testList = testList
                )
            } else {
                ClassTabContent(
                    navController = navController,
                    classList = classList
                )
            }
        }
    }
}
