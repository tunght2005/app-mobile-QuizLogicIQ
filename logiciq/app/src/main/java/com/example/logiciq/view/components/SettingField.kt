package com.example.logiciq.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*

@Composable
fun SettingField(
    label: String,
    value: String,
    icon: ImageVector = Icons.Default.ArrowForward,
    onValueChange: (String) -> Unit,
    onCommitChange: (String) -> Unit = {}
) {
    var isEditing by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(value) }

    // Sync lại nếu giá trị ngoài thay đổi
    LaunchedEffect(value) {
        text = value
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Color.White), shape = RoundedCornerShape(14.dp))
            .background(Color(0xFF3F6ABA), shape = RoundedCornerShape(14.dp))
            .padding(top = 12.dp, bottom = 12.dp, end = 20.dp, start = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                if (isEditing) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.LightGray,
                            cursorColor = Color.White
                        ),
                        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text(
                        text = value,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W400,
                        color = Color.LightGray
                    )
                }
            }

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(40.dp)
                    .border(BorderStroke(3.dp, Color.White), shape = CircleShape)
                    .padding(5.dp)
                    .clickable {
                        if (isEditing) {
                            onValueChange(text)      // Cập nhật biến ngoài
                            onCommitChange(text)     // Gọi ViewModel cập nhật Firestore
                        }
                        isEditing = !isEditing
                    }
            )
        }
    }
}
