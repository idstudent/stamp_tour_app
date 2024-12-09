package com.ljystamp.stamp_tour_app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.ljystamp.stamp_tour_app.api.model.SavedLocation
import com.ljystamp.stamp_tour_app.api.model.TourMapper
import com.ljystamp.stamp_tour_app.repository.LocationTourListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

// LocationTourListViewModel.kt
// LocationTourListViewModel.kt
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
        // 먼저 로그인 상태 체크
        auth.currentUser?.let {
            startObservingSavedLocations()
        }
    }

    private fun startObservingSavedLocations() {
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
                            title = document.getString("title") ?: "",
                            address = document.getString("address") ?: "",
                            image = document.getString("image") ?: "",
                            latitude = (document.get("latitude") as? Number)?.toDouble() ?: 0.0,
                            longitude = (document.get("longitude") as? Number)?.toDouble() ?: 0.0,
                            isStamped = document.getBoolean("isStamped") ?: false,
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

    fun saveTourLocation(tour: TourMapper, onComplete: (Boolean, String?) -> Unit) {
        val userId = auth.currentUser?.uid ?: run {
            onComplete(false, "로그인이 필요합니다")
            return
        }

        val savedLocation = hashMapOf(
            "userId" to userId,
            "contentId" to tour.contentid,
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
                onComplete(true, "장소가 저장되었습니다")
                _savedLocations.value = _savedLocations.value + SavedLocation(
                    contentId = tour.contentid,
                    title = tour.title,
                    address = tour.addr1,
                    image = tour.firstimage,
                    latitude = tour.mapy,
                    longitude = tour.mapx,
                    isStamped = false,
                    savedAt = null
                )
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error saving location", e)
                onComplete(false, "저장에 실패했습니다: ${e.message}")
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

    override fun onCleared() {
        super.onCleared()
        snapshotListener?.remove()
    }
}