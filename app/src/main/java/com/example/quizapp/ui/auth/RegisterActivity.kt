package com.example.logiciq.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.logiciq.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val emailEdit = findViewById<EditText>(R.id.registerEmailEditText)
        val passEdit = findViewById<EditText>(R.id.registerPasswordEditText)
        val registerBtn = findViewById<Button>(R.id.registerButton)

        registerBtn.setOnClickListener {
            val email = emailEdit.text.toString().trim()
            val pass = passEdit.text.toString().trim()

            if (email.isNotEmpty() && pass.length >= 6) {
                auth.createUserWithEmailAndPassword(email, pass)
                    .addOnSuccessListener { authResult ->
                        val firebaseUser = authResult.user
                        if (firebaseUser != null) {
                            val userData = hashMapOf(
                                "uid" to firebaseUser.uid,
                                "email" to firebaseUser.email,
                                "createdAt" to Timestamp.now(),
                                "lastLogin" to Timestamp.now()
                            )

                            firestore.collection("users")
                                .document(firebaseUser.uid)
                                .set(userData)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this,
                                        "Đăng ký thành công",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        this,
                                        "Lỗi lưu Firestore: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Lỗi đăng ký: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(
                    this,
                    "Vui lòng nhập email và mật khẩu >= 6 ký tự",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
