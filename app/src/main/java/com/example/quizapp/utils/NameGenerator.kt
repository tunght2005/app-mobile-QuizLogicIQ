package com.example.logiciq.utils

fun generateRandomName(): String {
    val adjectives = listOf("Happy", "Smart", "Brave", "Clever", "Cool")
    val animals = listOf("Tiger", "Fox", "Panda", "Dolphin", "Owl")
    val number = (100..999).random()
    return "${adjectives.random()}${animals.random()}$number"
}
