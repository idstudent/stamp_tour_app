package com.ljystamp.stamp_tour_app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.ljystamp.stamp_tour_app.api.model.TourMapper
import com.ljystamp.stamp_tour_app.repository.LocationTourListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class LocationTourListViewModel @Inject constructor(
    private val locationTourListRepository: LocationTourListRepository
): ViewModel()  {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun getLocationTourList(longitude: Double, latitude: Double, contentTypeId: Int): Flow<List<TourMapper>> {
        return locationTourListRepository.getLocationTourList(longitude, latitude, contentTypeId)
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
            }
            .addOnFailureListener { e ->
                Log.e("ljy", "error? ${e.message}")
                onComplete(false, "저장에 실패했습니다: ${e.message}")
            }
    }

    fun checkIfLocationSaved(tourId: String, onComplete: (Boolean) -> Unit) {
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
}