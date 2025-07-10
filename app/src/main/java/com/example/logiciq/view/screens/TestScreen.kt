package com.example.logiciq.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.logiciq.R
import com.example.logiciq.viewmodel.QuizViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun TestScreen(
    navController: NavController,
    quizId: String,
    viewModel: QuizViewModel = viewModel()
) {
    val quiz by viewModel.currentQuiz.collectAsState()
    LaunchedEffect(quizId) {
        viewModel.loadQuizById(quizId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E293B))
            .padding(horizontal = 24.dp)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 58.dp, bottom = 32.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Text(
                text = quiz?.title ?: "Cau hỏi",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.Center)
                    .widthIn(max = 200.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        quiz?.let { loadedQuiz ->
            val pagerState = rememberPagerState(pageCount = { loadedQuiz.questions.size })
            val coroutineScope = rememberCoroutineScope()

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentPadding = PaddingValues(horizontal = 0.dp),
                pageSpacing = 80.dp
            ) { page ->
                val question = loadedQuiz.questions[page]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFB6EAFF)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = question.text,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0A1D42),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .widthIn(max = 320.dp),
                            maxLines = 5,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(loadedQuiz.questions.size) { index ->
                    Surface(
                        shape = CircleShape,
                        color = if (index == pagerState.currentPage) Color.Blue else Color.White,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(3.dp)
                    ) {}
                }
            }
        }


        Spacer(modifier = Modifier.height(20.dp))

        // Thông tin người tạo + số câu hỏi
        if (quiz != null) {
            val user = FirebaseAuth.getInstance().currentUser

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user?.photoUrl ?: R.drawable.avatar) // load ảnh từ Firebase hoặc fallback
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(quiz!!.createdByName, color = Color.White, fontSize = 16.sp)
                    Text("${quiz!!.questions.size} câu hỏi", color = Color.Gray, fontSize = 16.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Button Bắt đầu thi
        Button(
            onClick = {
                quiz?.let {
                    navController.navigate("exam_screen/${it.id}")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6FED)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.slidestart),
                    contentDescription = "Bắt đầu học",
                    tint = Color.Green,
                    modifier = Modifier
                        .size(74.dp)
                        .padding(end = 12.dp)
                )
                Text(
                    text = "Bắt đầu học",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 30.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(R.drawable.logic_iq),
            contentDescription = "Logo",
            modifier = Modifier
                .size(400.dp)
                .padding(bottom = 100.dp)
        )
    }
}

