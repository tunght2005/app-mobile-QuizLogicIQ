package com.example.logiciq.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.logiciq.ui.screens.SearchResult

@Composable
fun ResultOverlay(results: List<SearchResult>, onClose: () -> Unit, navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 150.dp)
            .zIndex(10f),
        color = Color.White,
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 8.dp
    ) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Kết quả tìm kiếm", fontWeight = FontWeight.Bold)
                    Text("Đóng", color = Color.Blue, modifier = Modifier.clickable { onClose() })
                }

                Spacer(modifier = Modifier.height(8.dp))
                if (results.isEmpty()) {
                    Text("Không tìm thấy kết quả", color = Color.Red)
                }
            }

            items(results) { item ->
                ResultCard(result = item, navController = navController)
            }
        }
    }
}
