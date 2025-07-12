package com.example.logiciq.data.mapper

import com.example.logiciq.data.model.Question
import com.example.logiciq.data.model.Quiz
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toQuizOrNull(): Quiz? {
    val id = getString("id") ?: return null
    val title = getString("title") ?: return null
    val maxDurationSeconds = getLong("maxDurationSeconds")?.toInt() ?: return null
    val createBy = getString("createBy") ?: ""
    val createByName = getString("createByName") ?: ""
    val classIds = get("classIds") as? List<String> ?: emptyList()
    val createdAt = getTimestamp("createdAt") ?: Timestamp.now()

    val rawQuestions = get("questions") as? List<*> ?: return null
    val questionsData = rawQuestions.mapNotNull { it as? Map<String, Any> }

    val questions = questionsData.mapNotNull { map ->
        val type = map["type"] as? String ?: return@mapNotNull null
        val qid = map["id"] as? String ?: return@mapNotNull null
        val text = map["text"] as? String ?: return@mapNotNull null
        when (type) {
            "TYPE4" -> {
                val optionsRaw = map["options"] as? Map<*, *> ?: return@mapNotNull null
                val options = optionsRaw.mapNotNull { (k, v) ->
                    if (k is String && v is String) k to v else null
                }.toMap()
                val correct = (map["correctOption"] as? String)?.firstOrNull() ?: return@mapNotNull null
                Question.Type4(qid, text, options["A"] ?: "", options["B"] ?: "", options["C"] ?: "", options["D"] ?: "", correct)
            }
            else -> null
        }
    }

    return Quiz(
        id = id,
        title = title,
        questions = questions,
        maxDurationSeconds = maxDurationSeconds,
        createdBy = createBy,
        createdByName = createByName,
        classIds = classIds,
        createdAt = createdAt
    )
}
