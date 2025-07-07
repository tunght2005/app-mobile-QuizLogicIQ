package com.example.logiciq.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate

@Composable
fun CalendarRow() {
    val currentDate = remember { LocalDate.now() }
    val startOfWeek = remember { currentDate.with(DayOfWeek.MONDAY) }
    val days = remember(currentDate) {
        (0..6).map { offset -> startOfWeek.plusDays(offset.toLong()) }
    }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Lịch Hiện Hành", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(5.dp))
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            itemsIndexed(days) { _, date ->
                val isSelected = date == currentDate
                val dayNumber = date.dayOfMonth.toString()

                Column(
                    modifier = Modifier
                        .padding(horizontal = 6.dp, vertical = 8.dp)
                        .width(48.dp)
                        .height(88.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) Color(0xFFFFE6E6) else Color.Transparent),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = dayNumber,
                        color = if (isSelected) Color(0xFFEB3B5A) else Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    Text(
                        text = when (date.dayOfWeek.value) {
                            1 -> "Th 2"
                            2 -> "Th 3"
                            3 -> "Th 4"
                            4 -> "Th 5"
                            5 -> "Th 6"
                            6 -> "Th 7"
                            7 -> "CN"
                            else -> ""
                        },
                        color = if (isSelected) Color(0xFFEB3B5A) else Color(0xFF4A90E2),
                        fontSize = 14.sp
                    )
                    if (isSelected) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFFEB3B5A), shape = CircleShape)
                        )
                    }
                }
            }
        }
    }
}