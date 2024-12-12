package com.ljystamp.stamp_tour_app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class UserViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun signUp(email: String, password: String, nickname: String, onComplete: (Boolean, String?) -> Unit) {
        // 닉네임 중복 체크 로그
        Log.e("SignUpViewModel", "Checking nickname: $nickname")

        db.collection("users")
            .whereEqualTo("nickname", nickname)
            .get()
            .addOnSuccessListener { documents ->
                Log.e("SignUpViewModel", "Nickname check result: ${documents.size()} documents found")

                if (documents.isEmpty) {
                    Log.e("SignUpViewModel", "Nickname is available, creating account")
                    createAccount(email, password, nickname, onComplete)
                } else {
                    Log.e("SignUpViewModel", "Nickname is already in use")
                    onComplete(false, "이미 사용 중인 닉네임입니다")
                }
            }
            .addOnFailureListener { e ->
                Log.e("SignUpViewModel", "Error checking nickname", e)
                onComplete(false, "회원가입 중 오류가 발생했습니다: ${e.message}")
            }
    }
    fun logout(onComplete: (Boolean) -> Unit) {
        try {
            auth.signOut()

            onComplete(true)
        } catch (e: Exception) {
            Log.e("ljy", "Logout failed", e)
            onComplete(false)
        }
    }
    private fun createAccount(email: String, password: String, nickname: String, onComplete: (Boolean, String?) -> Unit) {
        Log.e("SignUpViewModel", "Creating account for email: $email")

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e("SignUpViewModel", "Account created successfully")

                    auth.currentUser?.sendEmailVerification()

                    val user = hashMapOf(
                        "uid" to auth.currentUser!!.uid,
                        "email" to email,
                        "nickname" to nickname,
                        "createdAt" to FieldValue.serverTimestamp()
                    )

                    Log.e("SignUpViewModel", "Saving user data to Firestore")
                    db.collection("users")
                        .document(auth.currentUser!!.uid)
                        .set(user)
                        .addOnSuccessListener {
                            Log.e("SignUpViewModel", "User data saved successfully")
                            onComplete(true, null)
                        }
                        .addOnFailureListener { e ->
                            Log.e("SignUpViewModel", "Error saving user data", e)
                            auth.currentUser?.delete()
                            onComplete(false, e.message)
                        }
                } else {
                    Log.e("SignUpViewModel", "Account creation failed", task.exception)
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthWeakPasswordException -> "비밀번호는 6자 이상이어야 합니다"
                        is FirebaseAuthInvalidCredentialsException -> "잘못된 이메일 형식입니다"
                        is FirebaseAuthUserCollisionException -> "이미 사용 중인 이메일입니다"
                        else -> task.exception?.message ?: "알 수 없는 오류가 발생했습니다"
                    }
                    onComplete(false, errorMessage)
                }
            }
    }
}