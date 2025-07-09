@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.logiciq.view.screens

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

val mockData = listOf(
    SearchResult("Học Phần", "Tên học phần A", "3 thuật ngữ", "Nguyễn Văn A"),
    SearchResult("Lớp Học", "Lớp KTPM1", "2 học phần", "11 thành viên"),
    SearchResult("Bài thi", "Bài kiểm tra đầu kỳ", "5 câu hỏi", "Trần B"),
    SearchResult("Bài thi", "Quiz tổng hợp", "10 câu hỏi", "Ngô C")
)

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: LibraryViewModel = viewModel()
    val quizViewModel: QuizViewModel = viewModel()

    val classList by viewModel.classList.collectAsState(initial = emptyList())
    val testList by viewModel.testList.collectAsState(initial = emptyList())

    var showAddSheet by remember { mutableStateOf(false) }
    var search by rememberSaveable { mutableStateOf("") }
    var results by remember { mutableStateOf<List<SearchResult>>(emptyList()) }
    var showResult by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    val userId = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(userId) {
        userId?.let {
            viewModel.loadTests(it)
            viewModel.loadClasses()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF84B6F4))
    ) {
        LazyColumn(
            state = listState,
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
                        results = mockData.filter {
                            it.title.removeDiacritics().contains(query)
                        }
                        showResult = true
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
            item { ReminderSection() }
            item { ClassSection(navController = navController, classList = classList) }
            item {
                ExamSection(
                    navController = navController,
                    testList = testList,
                    viewModel = quizViewModel,
                    onDeleteSuccess = {
                        userId?.let { viewModel.loadTests(it) }
                    }
                )
            }
        }

        if (showResult) {
            ResultOverlay(
                results = results,
                onClose = { showResult = false },
                navController = navController
            )
        }

        if (showError) {
            SearchErrorDialog(
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
