@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.logiciq.view.screens


import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.logiciq.viewmodel.ReminderViewModel
import com.example.logiciq.viewmodel.ReminderViewModelFactory
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.logiciq.data.model.SearchResult
import com.example.logiciq.navigation.Routes
import com.example.logiciq.view.components.*
import com.example.logiciq.viewmodel.LibraryViewModel
import com.example.logiciq.viewmodel.QuizViewModel
import com.example.logiciq.viewmodel.SearchViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.Normalizer
import java.util.regex.Pattern

fun String.removeDiacritics(): String {
    val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
    return Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        .matcher(normalized)
        .replaceAll("")
        .lowercase()
}

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: LibraryViewModel = viewModel()
    val quizViewModel: QuizViewModel = viewModel()

    val classList by viewModel.classList.collectAsState(initial = emptyList())
    val testList by viewModel.testList.collectAsState(initial = emptyList())

    var showAddSheet by remember { mutableStateOf(false) }
    val searchViewModel: SearchViewModel = viewModel()
    val results by searchViewModel.searchResults.collectAsState()

    var search by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val reminderViewModel: ReminderViewModel = viewModel(factory = ReminderViewModelFactory(context))
    val reminderList by remember { derivedStateOf { reminderViewModel.reminders } }

    val userId = FirebaseAuth.getInstance().currentUser?.uid

    // Xin quyền thông báo nếu Android 13+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) {}

        LaunchedEffect(Unit) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
   // Reload nếu rời lớp xong quay lại
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    LaunchedEffect(savedStateHandle?.get<Boolean>("refresh")) {
        if (savedStateHandle?.get<Boolean>("refresh") == true && userId != null) {
            viewModel.loadClasses(userId)
            savedStateHandle["refresh"] = false
        }
    }
    LaunchedEffect(userId) {
        userId?.let {
            viewModel.loadTests(it)
            viewModel.loadClasses(it)
            reminderViewModel.loadRemindersForToday()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF84B6F4))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 120.dp)
        ) {
            item {
                TopBar(
                    search = search,
                    onSearchChange = { search = it },
                    onSearchSubmit = {
                        val query = search.removeDiacritics()
                        if (query.isNotBlank()) {
                            searchViewModel.search(query)
                            showResult = true
                        } else {
                            showError = true
                        }
                    },
                    onClear = {
                        showResult = false
                        search = ""
                    },
                    onShowError = {
                        showError = true
                    }
                )
            }
            item { CalendarRow() }
            item {
                ReminderSection(
                    reminderList = reminderList,
                    onAddReminder = { title, start, end ->
                        reminderViewModel.addReminder(title, start, end)
                    },
                    onDeleteReminder = { reminder -> reminderViewModel.deleteReminder(reminder) }

                )
            }
            item { ClassSection(navController = navController, classList = classList) }
            item {
                ExamSection(
                    navController = navController,
                    testList = testList,
                    viewModel = quizViewModel,
                    quizId = testList.firstOrNull()?.id ?: "",
                    onDeleteSuccess = {
                        userId?.let { viewModel.loadTests(it) }
                    }
                )
            }
        }

        if (showResult) {
            ResultOverlay(
                results = results,
                onClose = {
                    showResult = false
                    search = ""
                },
                navController = navController
            )
        }


        if (showError) {
            ErrorDialog(
                title = "Lỗi tìm kiếm",
                message = "Vui lòng nhập từ khóa để tìm kiếm.",
                onDismiss = { showError = false }
            )
        }

        BottomNavigationBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter),
            onAddClick = {
                showAddSheet = true
            }
        )

        if (showAddSheet) {
            AddOptionsBottomSheet(
                onCreateClass = {
                    navController.navigate(Routes.NEWCLASS)
                    showAddSheet = false
                },
                onCreateTest = {
                    navController.navigate(Routes.NEWTEST)
                    showAddSheet = false
                },
                onDismiss = {
                    showAddSheet = false
                }
            )
        }
    }
}
