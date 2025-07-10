package com.example.logiciq.view.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.logiciq.R
import com.example.logiciq.data.model.Question
import com.example.logiciq.data.model.QuizResult
import com.example.logiciq.data.model.UserScore
import com.example.logiciq.data.model.toUiQuestion
import com.example.logiciq.view.components.ErrorDialog
import com.example.logiciq.viewmodel.QuizViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun ExamScreen(
    navController: NavController,
    quizId: String,
    viewModel: QuizViewModel = viewModel()
) {
    val quiz by viewModel.currentQuiz.collectAsState()
    val leaderboard by viewModel.leaderboard.collectAsState()

    var correctCount by remember { mutableStateOf(0) }
    var wrongCount by remember { mutableStateOf(0) }
    var timeLeftSeconds by remember { mutableStateOf(60 * 60) }
    var showWarning by remember { mutableStateOf(false) }

    LaunchedEffect(quizId) {
        viewModel.loadQuizById(quizId)
    }

    val questionList = remember(quiz) {
        quiz?.questions?.mapNotNull {
            (it as? Question.Type4)?.toUiQuestion()
        }?.map { it.copy(options = it.options.shuffled()) } ?: emptyList()
    }

    var questionIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }

    val currentQuestion = questionList.getOrNull(questionIndex)

    //Cảnh báo
    LaunchedEffect(showResult.not() && timeLeftSeconds > 0) {
        while (timeLeftSeconds > 0 && !showResult) {
            delay(1000)
            timeLeftSeconds--

            if (timeLeftSeconds == 5 * 60) {
                showWarning = true
            }
        }

        if (timeLeftSeconds == 0 && !showResult) {
            showResult = true
            FirebaseAuth.getInstance().currentUser?.let { user ->
                val result = QuizResult(
                    quizId = quizId,
                    userId = user.uid,
                    userEmail = user.email,
                    userName = user.displayName ?: "Người dùng",
                    answers = emptyList(),
                    correctCount = correctCount,
                    totalQuestions = questionList.size,
                    score = (correctCount * 100) / questionList.size,
                    timeTakenSeconds = (60 * 60 - timeLeftSeconds).toLong()
                )
                viewModel.saveQuizResult(result) { success, error ->
                    if (success) viewModel.loadLeaderboard(quizId)
                }
            }
        }
    }

    LaunchedEffect(selectedAnswer) {
        if (selectedAnswer.isNotEmpty()) {
            delay(500)
            if (selectedAnswer == currentQuestion?.correctAnswer) correctCount++ else wrongCount++
            selectedAnswer = ""
            if (questionIndex < questionList.lastIndex) questionIndex++ else {
                showResult = true
                FirebaseAuth.getInstance().currentUser?.let { user ->
                    val result = QuizResult(
                        quizId = quizId,
                        userId = user.uid,
                        userEmail = user.email,
                        userName = user.displayName ?: "Người dùng",
                        answers = emptyList(),
                        correctCount = correctCount,
                        totalQuestions = questionList.size,
                        score = (correctCount * 100) / questionList.size,
                        timeTakenSeconds = (60 * 60 - timeLeftSeconds).toLong()
                    )
                    viewModel.saveQuizResult(result) { success, error ->
                        if (success) viewModel.loadLeaderboard(quizId)
                    }
                }
            }
        }
    }

    val currentUser = FirebaseAuth.getInstance().currentUser
    val currentUserScore = remember(showResult) {
        if (showResult && currentUser != null) {
            UserScore(
                userId = currentUser.uid,
                name = currentUser.displayName ?: "Bạn",
                correct = correctCount,
                wrong = wrongCount
            )
        } else null
    }

    val minutes = timeLeftSeconds / 60
    val seconds = timeLeftSeconds % 60

    if (showWarning) {
        ErrorDialog(
            title = "Còn 5 phút",
            message = "Hoàn thành bài thi nhanh để có kết quả tốt.",
            onDismiss = {  showWarning = false}
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF1E293B)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 58.dp, start = 24.dp, end = 24.dp, bottom = 10.dp)
            ) {
                if(showResult){
                    IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.CenterStart)) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(40.dp))
                    }
                } else {
                    null
                }

                if (!showResult) {
                    Row(modifier = Modifier.align(Alignment.Center), verticalAlignment = Alignment.CenterVertically) {
                        Icon(painterResource(R.drawable.check), contentDescription = null, tint = Color.Green, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("$correctCount", fontSize = 28.sp, color = Color.White)
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(painterResource(R.drawable.cancel), contentDescription = null, tint = Color.Red, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("$wrongCount", fontSize = 28.sp, color = Color.White)
                    }
                    Text(
                        text = String.format("%02d:%02d", minutes, seconds),
                        color = if (timeLeftSeconds == 5 * 60) Color.Red else Color.Yellow,
                        fontSize = 18.sp,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                } else {
                    Text("Bảng Xếp Hạng", modifier = Modifier.align(Alignment.Center), color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Medium)
                }
            }

            if (showResult) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 24.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        itemsIndexed(leaderboard) { index, user ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .background(
                                        color = when (index) {
                                            0 -> Color(0xFF3B82F6)
                                            1 -> Color(0xFF60A5FA)
                                            2 -> Color(0xFF93C5FD)
                                            else -> Color(0xFF3F6ABA)
                                        },
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = listOf("🏆", "🥈", "🥉").getOrNull(index) ?: "${index + 1}",
                                    fontSize = 20.sp,
                                    color = Color.Yellow,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(R.drawable.avatar)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(user.name, fontSize = 20.sp, color = Color.White, modifier = Modifier.weight(1f))
                                Icon(painterResource(R.drawable.check), contentDescription = null, tint = Color.Green, modifier = Modifier.size(28.dp))
                                Text("${user.correct}", fontSize = 20.sp, color = Color.White, modifier = Modifier.padding(start = 8.dp))
                                Icon(painterResource(R.drawable.cancel), contentDescription = null, tint = Color.Red, modifier = Modifier.size(28.dp).padding(start = 8.dp))
                                Text("${user.wrong}", fontSize = 20.sp, color = Color.White, modifier = Modifier.padding(start = 4.dp))
                            }
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 24.dp)
                ) {
                    currentUserScore?.let { user ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(
                                    color = Color(0xFF10B981),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Điểm:",
                                fontSize = 20.sp,
                                color = Color.Yellow,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(R.drawable.avatar)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(user.name, fontSize = 20.sp, color = Color.White, modifier = Modifier.weight(1f))
                            Icon(painterResource(R.drawable.check), contentDescription = null, tint = Color.Green, modifier = Modifier.size(28.dp))
                            Text("${user.correct}", fontSize = 20.sp, color = Color.White, modifier = Modifier.padding(start = 8.dp))
                            Icon(painterResource(R.drawable.cancel), contentDescription = null, tint = Color.Red, modifier = Modifier.size(28.dp).padding(start = 8.dp))
                            Text("${user.wrong}", fontSize = 20.sp, color = Color.White, modifier = Modifier.padding(start = 4.dp))
                        }
                    }
                }
            } else {
                Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFFB6EAFF), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(currentQuestion?.term ?: "", color = Color(0xFF0A1D42), fontSize = 24.sp, fontWeight = FontWeight.Bold,modifier = Modifier
                            .align(Alignment.Center)
                            .widthIn(max = 320.dp),
                            maxLines = 5,
                            overflow = TextOverflow.Ellipsis,)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Chọn câu trả lời", color = Color.LightGray, modifier = Modifier.align(Alignment.Start))
                    Spacer(modifier = Modifier.height(16.dp))

                    currentQuestion?.options?.chunked(2)?.forEach { rowOptions ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            rowOptions.forEach { answer ->
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(vertical = 10.dp)
                                        .border(
                                            BorderStroke(3.dp, when {
                                                selectedAnswer == answer && answer == currentQuestion.correctAnswer -> Color.Green
                                                selectedAnswer == answer && answer != currentQuestion.correctAnswer -> Color.Red
                                                else -> Color.White
                                            }),
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable(enabled = selectedAnswer.isEmpty()) { selectedAnswer = answer },
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF1A2A55),
                                    tonalElevation = 2.dp,
                                    shadowElevation = 4.dp
                                ) {
                                    Text(
                                        text = answer,
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .heightIn(120.dp)
                                            .fillMaxWidth(),
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center,
                                        maxLines = 5,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Câu hỏi ${questionIndex + 1}/${questionList.size}",
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.logic_iq),
            contentDescription = "Logo",
            modifier = Modifier.size(400.dp).align(Alignment.CenterHorizontally).padding(bottom = 100.dp)
        )
    }
}
