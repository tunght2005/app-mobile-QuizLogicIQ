package com.example.logiciq.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.logiciq.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Chỉ gọi 1 lần
    }
}