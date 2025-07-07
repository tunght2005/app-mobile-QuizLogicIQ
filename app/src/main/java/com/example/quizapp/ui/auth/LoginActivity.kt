package com.example.logiciq.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.logiciq.R
import com.example.logiciq.ui.`class`.FirestoreTestActivity
import com.example.logiciq.ui.quiz.QuizDetailActivity
import com.example.logiciq.ui.schedule.FakeScheduleActivity
import com.example.logiciq.utils.collectIn
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.SignInButton

class LoginActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient

    private val googleLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken ?: throw Exception("Không có ID Token")
            viewModel.loginWithGoogle(idToken)
        } catch (e: Exception) {
            showError(e.message)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEdit = findViewById<EditText>(R.id.emailEditText)
        val passEdit = findViewById<EditText>(R.id.passwordEditText)
        val loginBtn = findViewById<Button>(R.id.loginButton)
        val googleBtn = findViewById<SignInButton>(R.id.googleSignInButton)
        val registerText = findViewById<TextView>(R.id.registerTextView)
        val forgotText = findViewById<TextView>(R.id.forgotPasswordTextView)

        setupGoogleSignIn()
        observeAuthState()

        loginBtn.setOnClickListener {
            val email = emailEdit.text.toString().trim()
            val pass = passEdit.text.toString().trim()
            viewModel.loginWithEmail(email, pass)
        }

        googleBtn.setOnClickListener {
            googleLauncher.launch(googleSignInClient.signInIntent)
        }

        registerText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        forgotText.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun observeAuthState() {
        viewModel.userState.collectIn(this) { result ->
            result?.onSuccess {
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                goToMain()
            }?.onFailure {
                showError(it.message)
            }
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, QuizDetailActivity::class.java))
        finish()
    }

    private fun showError(msg: String?) {
        Toast.makeText(this, msg ?: "Đăng nhập thất bại", Toast.LENGTH_LONG).show()
    }
}
