package com.example.logiciq.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.logiciq.data.model.ClassItem
import com.example.logiciq.data.model.Member
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun AllClassList(
    modifier: Modifier = Modifier,
    onClassClick: (ClassItem) -> Unit = {}
) {
    var classList by remember { mutableStateOf<List<ClassItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val snapshot = FirebaseFirestore.getInstance()
            .collection("classes")
            .get()
            .await()

        classList = snapshot.documents.mapNotNull { doc ->
            try {
                val id = doc.id
                val name = doc.getString("name") ?: return@mapNotNull null
                val description = doc.getString("description") ?: ""
                val createdBy = doc.getString("createdBy") ?: ""
                val creatorName = doc.getString("creatorName") ?: ""
                val createdAt = doc.getTimestamp("createdAt")
                val membersData = doc.get("members") as? List<Map<String, Any>> ?: emptyList()

                val members = membersData.mapNotNull { memberMap ->
                    val userId = memberMap["userId"] as? String ?: return@mapNotNull null
                    val userName = memberMap["userName"] as? String ?: ""
                    val avatar = memberMap["avatar"] as? String
                    Member(userId, userName, avatar)
                }

                ClassItem(
                    id = id,
                    name = name,
                    description = description,
                    createdBy = createdBy,
                    creatorName = creatorName,
                    createdAt = createdAt,
                    members = members
                )
            } catch (e: Exception) {
                null
            }
        }
        isLoading = false
    }

    if (isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.White)
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(classList) { classItem ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClassClick(classItem) },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E88E5))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = classItem.name, color = Color.White, style = MaterialTheme.typography.titleMedium)
                        if (classItem.description.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = classItem.description, color = Color.White.copy(0.8f))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${classItem.members.size} thành viên • Tạo bởi ${classItem.creatorName}",
                            color = Color.White.copy(0.7f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
