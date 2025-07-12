package com.example.logiciq.view.components

import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.example.logiciq.navigation.Routes
import com.example.logiciq.data.model.SearchResult

@Composable
fun ResultCard(result: SearchResult, navController: NavController) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = result.type, fontWeight = FontWeight.Bold)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF3B6DB0), RoundedCornerShape(10.dp))
                .padding(16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF3F6ABA)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .width(330.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null // remove deprecated ripple
                    ) {
                        when (result.type) {
                            "Lớp Học" -> navController.navigate("class_screen/${result.id}")
                            else -> navController.navigate("test_screen/${result.id}")
                        }

                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            result.title,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (result.type == "Lớp Học") {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 20.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(color = Color(0xFFBDD0FF))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Menu, contentDescription = null, tint = Color.Black)
                                        Spacer(Modifier.width(4.dp))
                                        Text(result.subtitle, color = Color.Black, fontWeight = FontWeight.SemiBold)
                                    }
                                }

                                Spacer(Modifier.width(15.dp))

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(color = Color(0xFFBDD0FF))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.AccountBox, contentDescription = null, tint = Color.Black)
                                        Spacer(Modifier.width(4.dp))
                                        Text(result.user, color = Color.Black, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            }
                        } else {
                            Text(result.subtitle, color = Color.White)
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp)
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                                Text(
                                    result.user,
                                    color = Color.White,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}