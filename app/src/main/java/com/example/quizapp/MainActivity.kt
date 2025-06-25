package com.example.logiciq.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.logiciq.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register) // Gắn XML layout
        setContentView(R.layout.activity_login)
        setContentView(R.layout.activity_reset_password)
        setContentView(R.layout.activity_main)
    }
}
