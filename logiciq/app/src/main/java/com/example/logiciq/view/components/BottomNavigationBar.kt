package com.example.logiciq.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.logiciq.navigation.Routes

@Composable
fun BottomNavigationBar(navController: NavController, modifier: Modifier = Modifier, onAddClick: () -> Unit) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color(0xFF3F6ABA),
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(Routes.HOME) },
            icon = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFF6FA8DC), shape = CircleShape)
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(36.dp), tint = Color.White)
                }
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = onAddClick,
            icon = {
                Icon(
                    Icons.Default.AddCircle,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = Color.White
                )
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(Routes.LIBRARY)},
            icon = { Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(36.dp), tint = Color.White) }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(Routes.PROFILE) },
//            icon = {
//                Icon(
//                    painter = painterResource(id = R.drawable.logic_iq),
//                    contentDescription = "Icon minh họa cho tài khoản",
//                    modifier = Modifier.size(24.dp)
//                )
//            },
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(36.dp), tint = Color.White) }
        )
    }
}
