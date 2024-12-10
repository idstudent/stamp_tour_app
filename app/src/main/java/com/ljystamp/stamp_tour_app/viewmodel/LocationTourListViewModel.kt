package com.ljystamp.stamp_tour_app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.ljystamp.stamp_tour_app.api.model.SavedLocation
import com.ljystamp.stamp_tour_app.api.model.TourMapper
import com.ljystamp.stamp_tour_app.repository.LocationTourListRepository
import com.ljystamp.stamp_tour_app.util.SaveResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class LocationTourListViewModel @Inject constructor(
    private val locationTourListRepository: LocationTourListRepository
): ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val _savedLocations = MutableStateFlow<List<SavedLocation>>(emptyList())
    val savedLocations: StateFlow<List<SavedLocation>> = _savedLocations.asStateFlow()

    private var snapshotListener: ListenerRegistration? = null

    init {
        auth.currentUser?.let {
            startObservingSavedLocations()
        }
    }

    fun startObservingSavedLocations() {
        val userId = auth.currentUser?.uid ?: return

        snapshotListener?.remove()

        snapshotListener = db.collection("saved_locations")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("Firebase", "Listen failed", e)
                    return@addSnapshotListener
                }

                try {
                    val locations = snapshot?.documents?.mapNotNull { document ->
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
                    } ?: emptyList()

                    _savedLocations.value = locations
                } catch (e: Exception) {
                    Log.e("Firebase", "Error parsing data: ${e.message}", e)
                }
            }
    }

    fun getLocationTourList(longitude: Double, latitude: Double, pageNo: Int, contentTypeId: Int): Flow<List<TourMapper>> {
        return locationTourListRepository.getLocationTourList(longitude, latitude, pageNo, contentTypeId)
            .catch { it.printStackTrace() }
    }

    fun saveTourLocation(tour: TourMapper, onComplete: (SaveResult) -> Unit) {
        val userId = auth.currentUser?.uid ?: run {
            onComplete(SaveResult.LoginRequired("로그인이 필요해요"))
            return
        }

        // 현재 저장된 장소 수 확인
        db.collection("saved_locations")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.size() >= 30) {
                    onComplete(SaveResult.MaxLimitReached("최대 30개까지 저장 가능해요"))
                    return@addOnSuccessListener
                }

                val savedLocation = hashMapOf(
                    "userId" to userId,
                    "contentId" to tour.contentid,
                    "contentTypeId" to tour.contenttypeid,
                    "title" to tour.title,
                    "address" to tour.addr1,
                    "image" to tour.firstimage,
                    "latitude" to tour.mapy,
                    "longitude" to tour.mapx,
                    "isStamped" to false,
                    "savedAt" to FieldValue.serverTimestamp()
                )

                db.collection("saved_locations")
                    .document("${userId}_${tour.contentid}")
                    .set(savedLocation)
                    .addOnSuccessListener {
                        onComplete(SaveResult.Success("장소가 저장 되었어요"))
                        _savedLocations.value = _savedLocations.value + SavedLocation(
                            contentId = tour.contentid,
                            contentTypeId = tour.contenttypeid,
                            title = tour.title,
                            address = tour.addr1,
                            image = tour.firstimage,
                            latitude = tour.mapy,
                            longitude = tour.mapx,
                            isVisited = false,
                            savedAt = null
                        )
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firebase", "Error saving location", e)
                        onComplete(SaveResult.Failure("저장에 실패했어요"))
                    }
            }
            .addOnFailureListener {
                onComplete(SaveResult.Failure("오류가 발생했어요"))
            }
    }

    fun checkIfLocationSaved(tourId: Int, onComplete: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: run {
            onComplete(false)
            return
        }

        db.collection("saved_locations")
            .document("${userId}_${tourId}")
            .get()
            .addOnSuccessListener { document ->
                onComplete(document.exists())
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    fun updateVisitStatus(contentId: Int, onComplete: (Boolean, String?) -> Unit) {
        val userId = auth.currentUser?.uid ?: run {
            onComplete(false, "로그인이 필요합니다")
            return
        }

        db.collection("saved_locations")
            .document("${userId}_${contentId}")
            .update("isVisited", true,
                "visitedAt", FieldValue.serverTimestamp())
            .addOnSuccessListener {
                // 로컬 상태도 업데이트
                val updatedLocations = _savedLocations.value.map { location ->
                    if (location.contentId == contentId) {
                        location.copy(isVisited = true)
                    } else {
                        location
                    }
                }
                _savedLocations.value = updatedLocations
                onComplete(true, "스탬프를 찍었어요!")
            }
            .addOnFailureListener { e ->
                onComplete(false, "스탬프 찍기에 실패했어요")
            }
    }

    override fun onCleared() {
        super.onCleared()

        snapshotListener?.remove()
    }
}