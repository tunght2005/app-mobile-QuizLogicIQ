package com.example.logiciq.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.logiciq.data.repository.UserRepository
import com.example.logiciq.databinding.ActivityProfileBinding
import com.example.logiciq.ui.auth.LoginActivity
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(UserRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.loadUser()

        lifecycleScope.launchWhenStarted {
            viewModel.user.collect { user ->
                if (user != null) {
                    binding.tvName.text = user.name
                    binding.tvEmail.text = user.email
                    binding.tvPassword.text = "*********"
                } else {
                    Toast.makeText(this@ProfileActivity, "Không có thông tin người dùng", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            finish()
        }

    }
}
