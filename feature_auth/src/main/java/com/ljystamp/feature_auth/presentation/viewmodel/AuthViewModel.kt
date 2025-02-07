package com.ljystamp.feature_auth.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel: ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun signUp(email: String, password: String, nickname: String, onComplete: (Boolean, String?) -> Unit) {
        db.collection("users")
            .whereEqualTo("nickname", nickname)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    createAccount(email, password, nickname, onComplete)
                } else {
                    onComplete(false, "이미 사용 중인 닉네임이에요")
                }
            }
            .addOnFailureListener { e ->
                onComplete(false, "회원가입 중 오류가 발생했어요: ${e.message}")
            }
    }

    private fun createAccount(email: String, password: String, nickname: String, onComplete: (Boolean, String?) -> Unit) {
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
                            auth.signOut()
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

    fun deleteAccount(password: String, onComplete: (Boolean, String?) -> Unit) {
        val user = auth.currentUser ?: run {
            onComplete(false, "로그인된 사용자가 없습니다")
            return
        }

        // 재인증 필요 (보안을 위해 Firebase에서 요구)
        val credential = EmailAuthProvider.getCredential(user.email!!, password)

        user.reauthenticate(credential).addOnCompleteListener { reAuthTask ->
            if (reAuthTask.isSuccessful) {
                // 1. 저장된 위치 정보 삭제
                db.collection("saved_locations")
                    .whereEqualTo("userId", user.uid)
                    .get()
                    .addOnSuccessListener { documents ->
                        // 배치 작업으로 문서들 삭제
                        val batch = db.batch()
                        documents.forEach { doc ->
                            batch.delete(doc.reference)
                        }

                        batch.commit().addOnCompleteListener { batchTask ->
                            if (batchTask.isSuccessful) {
                                // 2. 사용자 정보 삭제
                                db.collection("users")
                                    .document(user.uid)
                                    .delete()
                                    .addOnSuccessListener {
                                        // 3. Firebase Authentication 계정 삭제
                                        user.delete()
                                            .addOnSuccessListener {
                                                onComplete(true, null)
                                            }
                                            .addOnFailureListener { e ->
                                                onComplete(false, "계정 삭제 실패: ${e.message}")
                                            }
                                    }
                                    .addOnFailureListener { e ->
                                        onComplete(false, "사용자 정보 삭제 실패: ${e.message}")
                                    }
                            } else {
                                onComplete(false, "저장된 위치 정보 삭제 실패")
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        onComplete(false, "데이터 조회 실패: ${e.message}")
                    }
            } else {
                onComplete(false, "비밀번호가 일치하지 않습니다")
            }
        }
    }
}