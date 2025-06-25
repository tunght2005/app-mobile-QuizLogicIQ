package com.example.logiciq.ui.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.logiciq.R
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        val emailEdit = findViewById<EditText>(R.id.resetEmailEditText)
        val sendBtn = findViewById<Button>(R.id.sendResetButton)

        sendBtn.setOnClickListener {
            val email = emailEdit.text.toString().trim()
            if (email.isNotEmpty()) {
                viewModel.sendResetPassword(email)
            } else {
                Toast.makeText(this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show()
            }
        }

        // Quan sát StateFlow
        lifecycleScope.launchWhenStarted {
            viewModel.resetPasswordState.collect { result ->
                result?.let {
                    if (it.isSuccess) {
                        Toast.makeText(this@ResetPasswordActivity, "Đã gửi email khôi phục mật khẩu!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@ResetPasswordActivity, "Lỗi: ${it.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
