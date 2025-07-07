package com.example.logiciq.data.model

import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

enum class QuestionType { TYPE4 }

sealed class Question {
    abstract val id: String
    abstract val text: String
    abstract val type: QuestionType

    data class Type4(
        override val id: String = UUID.randomUUID().toString(),
        override val text: String,
        val optionA: String,
        val optionB: String,
        val optionC: String,
        val optionD: String,
        val correctOption: Char
    ) : Question() {
        override val type = QuestionType.TYPE4
        val options: Map<Char, String>
            get() = mapOf(
                'A' to optionA,
                'B' to optionB,
                'C' to optionC,
                'D' to optionD
            )
    }
}

// Convert Type4 to Map
fun Question.Type4.toMap(): Map<String, Any> {
    return mapOf(
        "id" to id,
        "text" to text,
        "type" to "TYPE4",
        "optionA" to optionA,
        "optionB" to optionB,
        "optionC" to optionC,
        "optionD" to optionD,
        "correctOption" to correctOption.toString()
    )
}

// Convert Map to Question
fun questionFromMap(map: Map<String, Any>): Question {
    return when (map["type"]) {
        "TYPE4" -> Question.Type4(
            id = map["id"] as String,
            text = map["text"] as String,
            optionA = map["optionA"] as String,
            optionB = map["optionB"] as String,
            optionC = map["optionC"] as String,
            optionD = map["optionD"] as String,
            correctOption = (map["correctOption"] as String).first()
        )
        else -> throw IllegalArgumentException("Loại câu hỏi không hợp lệ")
    }
}
