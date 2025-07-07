package com.example.logiciq.ui.quiz

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.logiciq.R

class QuizDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_detail)

        val quizId = intent.getStringExtra("quizId") ?: "Không có quizId"
        findViewById<TextView>(R.id.quizIdTextView).text = "Quiz ID: $quizId"
    }
}
