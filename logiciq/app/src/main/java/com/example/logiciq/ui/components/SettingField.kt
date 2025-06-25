package com.example.logiciq.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun SettingField(label: String, value: String, icon: ImageVector) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Color.White), shape = RoundedCornerShape(14.dp))
            .background(Color(0xFF3F6ABA), shape = RoundedCornerShape(14.dp))
            .padding(top = 12.dp, bottom = 12.dp, end = 30.dp, start = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(text = label, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.W400, color = Color.LightGray)
            }
            Icon(icon,
                contentDescription = null, tint = Color.White,
                modifier = Modifier
                    .size(40.dp)
                    .border(BorderStroke(3.dp, Color.White), shape = CircleShape)
                    .padding(5.dp)
            )
        }
    }
}
