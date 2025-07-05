package com.example.quizapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.logiciq.data.model.User
import com.google.firebase.auth.FirebaseAuth

class UserViewModel : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    fun loadCurrentUser() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser ?: return
        val user = User(
            uid = firebaseUser.uid,
            name = firebaseUser.displayName ?: firebaseUser.email?.substringBefore("@")
            ?: "Unknown",
            email = firebaseUser.email ?: "",
            photoUrl = firebaseUser.photoUrl?.toString()
        )
        _user.value = user
    }
}