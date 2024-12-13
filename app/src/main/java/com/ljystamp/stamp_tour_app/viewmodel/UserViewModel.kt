package com.ljystamp.stamp_tour_app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.ljystamp.stamp_tour_app.api.model.SavedLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel: ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _tourPlaceList = MutableStateFlow<List<SavedLocation>>(emptyList())
    val tourPlaceList = _tourPlaceList.asStateFlow()

    private val _cultureList = MutableStateFlow<List<SavedLocation>>(emptyList())
    val cultureList = _cultureList.asStateFlow()

    private val _eventList = MutableStateFlow<List<SavedLocation>>(emptyList())
    val eventList = _eventList.asStateFlow()

    private val _activityList = MutableStateFlow<List<SavedLocation>>(emptyList())
    val activityList = _activityList.asStateFlow()

    private val _foodList = MutableStateFlow<List<SavedLocation>>(emptyList())
    val foodList = _foodList.asStateFlow()

    private val _userProfile = MutableStateFlow<Map<String, Any>?>(null)
    val userProfile = _userProfile.asStateFlow()

    fun signUp(email: String, password: String, nickname: String, onComplete: (Boolean, String?) -> Unit) {
        db.collection("users")
            .whereEqualTo("nickname", nickname)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    createAccount(email, password, nickname, onComplete)
                } else {
                    onComplete(false, "이미 사용 중인 닉네임입니다")
                }
            }
            .addOnFailureListener { e ->
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

    fun getUserProfileAndSavedLocations() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { userDocument ->
                _userProfile.value = userDocument.data

                db.collection("saved_locations")
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnSuccessListener { locationDocuments ->
                        val savedLocations = locationDocuments.documents.mapNotNull { document ->
                            SavedLocation(
                                contentId = (document.get("contentId") as? Number)?.toInt() ?: 0,
                                contentTypeId = (document.get("contentTypeId") as? Number)?.toInt() ?: 0,
                                title = document.getString("title") ?: "",
                                address = document.getString("address") ?: "",
                                image = document.getString("image") ?: "",
                                latitude = (document.get("latitude") as? Number)?.toDouble() ?: 0.0,
                                longitude = (document.get("longitude") as? Number)?.toDouble() ?: 0.0,
                                isVisited = document.getBoolean("isVisited") ?: false,
                                savedAt = document.getTimestamp("savedAt")
                            )
                        }

                        // 카테고리별로 분류
                        _tourPlaceList.value = savedLocations.filter { it.contentTypeId == 12 }
                        _cultureList.value = savedLocations.filter { it.contentTypeId == 14 }
                        _eventList.value = savedLocations.filter { it.contentTypeId == 15 }
                        _activityList.value = savedLocations.filter { it.contentTypeId == 28 }
                        _foodList.value = savedLocations.filter { it.contentTypeId == 39 }
                    }
            }
    }
}