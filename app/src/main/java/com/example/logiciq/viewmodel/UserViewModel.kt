package com.example.logiciq.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logiciq.data.model.User
import com.example.logiciq.data.repository.AuthRepository
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val authRepo = AuthRepository()

    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user

    private val _updateResult = mutableStateOf<Result<Unit>?>(null)
    val updateResult: State<Result<Unit>?> = _updateResult

    private val _isUploading = mutableStateOf(false)
    val isUploading: Boolean
        get() = _isUploading.value

    var uploadStatus by mutableStateOf<String?>(null)
        private set

    fun clearUploadStatus() {
        uploadStatus = null
    }

    fun loadUser() {
        viewModelScope.launch {
            val firebaseUser = authRepo.getCurrentFirebaseUser()
            firebaseUser?.let {
                _user.value = User(
                    uid = it.uid,
                    name = it.displayName ?: "",
                    email = it.email ?: "",
                    photoUrl = it.photoUrl?.toString()
                )
            }
        }
    }

    fun updateUser(name: String, email: String) {
        viewModelScope.launch {
            _updateResult.value = authRepo.updateUserProfile(name, email)
            if (_updateResult.value?.isSuccess == true) {
                loadUser()
            }
        }
    }

    fun uploadAvatar(uri: Uri, context: Context) {
        viewModelScope.launch {
            _isUploading.value = true
            val result = authRepo.uploadAvatar(uri, context)
            _isUploading.value = false

            if (result.isSuccess) {
                val newPhotoUrl = result.getOrNull()
                println("✅ Upload avatar success: $newPhotoUrl")

                // Cập nhật lại user để hiển thị ảnh ngay lập tức
                _user.value = _user.value?.copy(photoUrl = newPhotoUrl)
                uploadStatus = "Ảnh đại diện đã được cập nhật"
            } else {
                val error = result.exceptionOrNull()?.message ?: "Lỗi không xác định"
                println("❌ Upload avatar failed: $error")
                uploadStatus = "Tải ảnh thất bại: $error"
            }
        }
    }

    fun resetUpdateResult() {
        _updateResult.value = null
    }

    fun logout() {
        authRepo.signOut()
    }
}
