package com.example.logiciq.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logiciq.data.model.User
import com.example.logiciq.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class AuthResult {
    object Loading : AuthResult()
    data class Success(val user: User) : AuthResult()
    data class Error(val message: String) : AuthResult()
    object Idle : AuthResult()
}

sealed class ResetPasswordResult {
    object Loading : ResetPasswordResult()
    object Success : ResetPasswordResult()
    data class Error(val message: String) : ResetPasswordResult()
    object Idle : ResetPasswordResult()
}

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _authState = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val authState: StateFlow<AuthResult> = _authState

    private val _resetPasswordState = MutableStateFlow<ResetPasswordResult>(ResetPasswordResult.Idle)
    val resetPasswordState: StateFlow<ResetPasswordResult> = _resetPasswordState

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthResult.Loading
            val result = repository.registerWithEmail(name, email, password, null)
            _authState.value = result.fold(
                onSuccess = { AuthResult.Success(it) },
                onFailure = { AuthResult.Error(it.message ?: "Lỗi không xác định") }
            )
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthResult.Loading
            val result = repository.loginWithEmail(email, password)
            _authState.value = result.fold(
                onSuccess = { AuthResult.Success(it) },
                onFailure = { AuthResult.Error(it.message ?: "Đăng nhập thất bại") }
            )
        }
    }

    fun sendResetPasswordEmail(email: String) {
        viewModelScope.launch {
            _resetPasswordState.value = ResetPasswordResult.Loading
            val result = repository.sendResetPasswordEmail(email)
            _resetPasswordState.value = result.fold(
                onSuccess = { ResetPasswordResult.Success },
                onFailure = { ResetPasswordResult.Error(it.message ?: "Gửi email thất bại") }
            )
        }
    }

    fun resetAuthState() {
        _authState.value = AuthResult.Idle
    }

    fun resetResetPasswordState() {
        _resetPasswordState.value = ResetPasswordResult.Idle
    }
    fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        viewModelScope.launch {
            try {
                val result = FirebaseAuth.getInstance().signInWithCredential(credential).await()
                val firebaseUser = result.user
                if (firebaseUser != null) {
                    val user = User(
                        uid = firebaseUser.uid,
                        name = firebaseUser.displayName ?: "",
                        email = firebaseUser.email ?: "",
                        photoUrl = firebaseUser.photoUrl?.toString()
                    )
                    _authState.value = AuthResult.Success(user)
                } else {
                    _authState.value = AuthResult.Error("Người dùng không tồn tại")
                }
            } catch (e: Exception) {
                _authState.value = AuthResult.Error(e.message ?: "Đăng nhập Google thất bại")
            }
        }
    }

}
