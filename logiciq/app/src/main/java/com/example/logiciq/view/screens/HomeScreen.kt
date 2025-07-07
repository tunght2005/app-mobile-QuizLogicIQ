package com.example.logiciq.view.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.logiciq.R
import org.threeten.bp.LocalDate
import org.threeten.bp.DayOfWeek
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.logiciq.navigation.Routes
import com.example.logiciq.view.components.AddOptionsBottomSheet
import java.text.Normalizer
import java.util.regex.Pattern
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logiciq.data.model.ClassItem
import com.example.logiciq.view.components.BottomNavigationBar
import com.example.logiciq.view.components.CalendarRow
import com.example.logiciq.view.components.ClassSection
import com.example.logiciq.view.components.ResultOverlay
import com.example.logiciq.view.components.SearchErrorDialog
import com.example.logiciq.view.components.CreateReminderDialog
import com.example.logiciq.view.components.ExamSection
import com.example.logiciq.view.components.ReminderSection
import com.example.logiciq.view.components.TopBar
import com.example.logiciq.viewmodel.LibraryViewModel

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: LibraryViewModel = viewModel()
    val classList by viewModel.classList.collectAsState(initial = emptyList())

    var showAddSheet by remember { mutableStateOf(false) }
    var search by rememberSaveable { mutableStateOf("") }
    var results by remember { mutableStateOf<List<SearchResult>>(emptyList()) }
    var showResult by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.loadClasses()
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
            item { ExamSection(navController) }
        }

        if (showResult) {
            ResultOverlay(
                results = results,
                onClose = { showResult = false },
                navController
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
                // nhấn nút Add
                showAddSheet = true
            }
        )
        // Modal Bottom Sheet
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

//giả lập model cho học phần, lớp, bài thi
data class SearchResult(
    val type: String,
    val title: String,
    val subtitle: String,
    val user: String
)

fun String.removeDiacritics(): String {
    val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
    return Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        .matcher(normalized)
        .replaceAll("")
        .lowercase()
}
val mockData = listOf(
    SearchResult("Học Phần", "Name học phần", "3 thuật ngữ", "name user"),
    SearchResult("Học Phần", "Name học ", "3 thuật ngữ", "name user"),
    SearchResult("Học Phần", "Name ", "3 thuật ngữ", "name user"),
    SearchResult("Lớp Học", "Name Lớp", "2 học phần","11 members"),
    SearchResult("Lớp Học", "Name Lớp", "2 học phần","11 members"),
    SearchResult("Lớp Học", "Name Lớp", "2 học phần","11 members"),
    SearchResult("Lớp Học", "Name Lớp", "2 học phần","11 members"),
    SearchResult("Lớp Học", "Name Lớp", "2 học phần","11 members"),
    SearchResult("Bài thi", "Name bài thi", "3 câu hỏi", "name user")
)

