package com.example.logiciq.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logiciq.data.model.User
import com.example.logiciq.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _userState = MutableStateFlow<Result<User>?>(null)
    val userState: StateFlow<Result<User>?> = _userState

    private val _resetPasswordState = MutableStateFlow<Result<Unit>?>(null)
    val resetPasswordState: StateFlow<Result<Unit>?> = _resetPasswordState

    // Đăng nhập bằng Google
    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            try {
                val result = authRepository.firebaseAuthWithGoogle(idToken)
                _userState.value = result
            } catch (e: Exception) {
                _userState.value = Result.failure(e)
            }
        }
    }

    // Đăng nhập bằng Email
    fun loginWithEmail(email: String, password: String) {
        viewModelScope.launch {
            try {
                val result = authRepository.loginWithEmail(email, password)
                _userState.value = result
            } catch (e: Exception) {
                _userState.value = Result.failure(e)
            }
        }
    }

    // Đăng ký mới
    fun registerWithEmail(name: String, email: String, password: String, phone: String?) {
        viewModelScope.launch {
            try {
                val result = authRepository.registerWithEmail(name, email, password, phone)
                _userState.value = result
            } catch (e: Exception) {
                _userState.value = Result.failure(e)
            }
        }
    }

    // Gửi email reset mật khẩu
    fun sendResetPassword(email: String) {
        viewModelScope.launch {
            _resetPasswordState.value = null // clear trước
            FirebaseAuth.getInstance().setLanguageCode("vi")
            val result = authRepository.sendResetPasswordEmail(email)
            _resetPasswordState.value = result
        }
    }
}
