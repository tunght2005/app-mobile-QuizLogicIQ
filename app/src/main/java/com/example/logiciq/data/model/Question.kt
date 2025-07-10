package com.example.logiciq.data.model

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

// UI Model for displaying Question
data class UiQuestion(
    val id: String,
    val term: String,
    val options: List<String>,
    val correctAnswer: String
)

fun Question.Type4.toUiQuestion(): UiQuestion {
    return UiQuestion(
        id = id,
        term = text,
        options = options.values.toList(),
        correctAnswer = options[correctOption] ?: ""
    )
}

// Convert Type4 to Map for Firestore
fun Question.Type4.toMap(): Map<String, Any> {
    return mapOf(
        "id" to id,
        "text" to text,
        "type" to "TYPE4",
        "options" to mapOf(
            "A" to optionA,
            "B" to optionB,
            "C" to optionC,
            "D" to optionD
        ),
        "correctOption" to correctOption.toString()
    )
}

// Convert Map to Question
fun questionFromMap(map: Map<String, Any?>): Question {
    return when (map["type"]) {
        "TYPE4" -> {
            val id = map["id"] as? String ?: throw IllegalArgumentException("Thiếu id")
            val text = map["text"] as? String ?: throw IllegalArgumentException("Thiếu text")
            val correctOptionStr = map["correctOption"] as? String ?: throw IllegalArgumentException("Thiếu correctOption")
            val correctOption = correctOptionStr.first()

            val optionsMap = map["options"] as? Map<String, String> ?: throw IllegalArgumentException("Thiếu options")

            Question.Type4(
                id = id,
                text = text,
                optionA = optionsMap["A"] ?: "",
                optionB = optionsMap["B"] ?: "",
                optionC = optionsMap["C"] ?: "",
                optionD = optionsMap["D"] ?: "",
                correctOption = correctOption
            )
        }

        else -> throw IllegalArgumentException("Loại câu hỏi không hợp lệ")
    }
}
