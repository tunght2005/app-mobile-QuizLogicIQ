package com.example.logiciq.data.model

import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.random.Random

enum class QuestionType { TYPE2, TYPE4 }

sealed class Question {
    abstract val id: String
    abstract val text: String
    abstract val type: QuestionType

    data class Type2(
        override val id: String = UUID.randomUUID().toString(),
        override val text: String,
        val correctAnswer: String,
        internal val wrongPool: List<String>
    ) : Question() {
        override val type = QuestionType.TYPE2
        val options: List<String> by lazy {
            listOf(correctAnswer, wrongPool.random()).shuffled()
        }
    }

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