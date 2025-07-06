package com.ljystamp.stamp_tour_app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.ljystamp.stamp_tour_app.model.SavedLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel: ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _allList = MutableStateFlow<List<SavedLocation>>(emptyList())
    val allList = _allList.asStateFlow()

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

    private val _certificationCount = MutableStateFlow(0)
    val certificationCount = _certificationCount.asStateFlow()

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
    fun logout(onComplete: (Boolean) -> Unit) {
        try {
            auth.signOut()
            clearAllData()
            onComplete(true)
        } catch (e: Exception) {
            Log.e("ljy", "Logout failed", e)
            onComplete(false)
        }
    }
    private fun clearAllData() {
        _userProfile.value = null
        _allList.value = emptyList()
        _tourPlaceList.value = emptyList()
        _cultureList.value = emptyList()
        _eventList.value = emptyList()
        _activityList.value = emptyList()
        _foodList.value = emptyList()
        _certificationCount.value = 0
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
                        _allList.value = savedLocations
                        _tourPlaceList.value = savedLocations.filter { it.contentTypeId == 12 }
                        _cultureList.value = savedLocations.filter { it.contentTypeId == 14 }
                        _eventList.value = savedLocations.filter { it.contentTypeId == 15 }
                        _activityList.value = savedLocations.filter { it.contentTypeId == 28 }
                        _foodList.value = savedLocations.filter { it.contentTypeId == 39 }

                        // 뱃지 카운트 계산
                        val tourBadges = calculateBadgeCount(_tourPlaceList.value.count { it.isVisited })
                        val cultureBadges = calculateBadgeCount(_cultureList.value.count { it.isVisited })
                        val eventBadges = calculateBadgeCount(_eventList.value.count { it.isVisited })
                        val activityBadges = calculateBadgeCount(_activityList.value.count { it.isVisited })
                        val foodBadges = calculateBadgeCount(_foodList.value.count { it.isVisited })

                        _certificationCount.value = tourBadges + cultureBadges + eventBadges + activityBadges + foodBadges

                    }
            }
    }
    private fun calculateBadgeCount(visitedCount: Int): Int {
        return when {
            visitedCount >= 50 -> 3
            visitedCount >= 30 -> 2
            visitedCount >= 10 -> 1
            else -> 0
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