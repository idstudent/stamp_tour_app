package com.ljystamp.common.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginViewModel: ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    fun signIn(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidCredentialsException -> "이메일 또는 비밀번호가 일치하지 않습니다"
                        is FirebaseAuthInvalidUserException -> "존재하지 않는 계정입니다"
                        else -> "로그인에 실패했습니다"
                    }
                    onComplete(false, errorMessage)
                }
            }
    }
}