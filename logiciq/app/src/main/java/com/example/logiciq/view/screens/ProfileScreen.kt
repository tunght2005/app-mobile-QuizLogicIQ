package com.example.logiciq.view.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.logiciq.R
import com.example.logiciq.navigation.Routes
import com.example.logiciq.viewmodel.UserViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: UserViewModel = viewModel()
) {
    val context = LocalContext.current
    val userState by viewModel.user
    val currentUser = userState

    // Khi chọn ảnh
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.uploadAvatar(it, context)
        }
    }

    // Load user khi mở màn hình
    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    // Hiện Toast khi upload xong
    LaunchedEffect(viewModel.uploadStatus) {
        viewModel.uploadStatus?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearUploadStatus()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E293B)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(40.dp),
                        tint = Color.White
                    )
                }
                Text(
                    text = "Hồ Sơ",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Avatar
            Box(
                modifier = Modifier
                    .size(115.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { launcher.launch("image/*") }
            ) {
                if (!currentUser?.photoUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(currentUser?.photoUrl)
                            .crossfade(true)
                            .diskCachePolicy(CachePolicy.DISABLED)
                            .memoryCachePolicy(CachePolicy.DISABLED)
                            .build(),
                        contentDescription = "Avatar",
                        modifier = Modifier.fillMaxSize(),
                        placeholder = painterResource(R.drawable.avatar),
                        error = painterResource(R.drawable.avatar)
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.avatar),
                        contentDescription = "Default Avatar",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Loader
            if (viewModel.isUploading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            }

            Text(
                text = currentUser?.name ?: "Tên người dùng",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
            )

            // Button Cài đặt
            Button(
                onClick = { navController.navigate(Routes.SETTING) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F6ABA)),
                modifier = Modifier
                    .width(330.dp)
                    .height(70.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color.White)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Cài đặt",
                        tint = Color.White,
                        modifier = Modifier
                            .size(74.dp)
                            .padding(end = 12.dp)
                    )
                    Text(
                        text = "Cài Đặt Của Bạn",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Button Lịch sử
            Button(
                onClick = { navController.navigate(Routes.HISTORY) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F6ABA)),
                modifier = Modifier
                    .width(330.dp)
                    .height(70.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color.White)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Lịch sử",
                        tint = Color.White,
                        modifier = Modifier
                            .size(74.dp)
                            .padding(end = 12.dp)
                    )
                    Text(
                        text = "Lịch Sử Hoạt Động",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
            }
        }

        // Logo cuối
        Image(
            painter = painterResource(R.drawable.logic_iq),
            contentDescription = "Logo",
            modifier = Modifier
                .size(400.dp)
                .padding(bottom = 100.dp)
        )
    }
}
