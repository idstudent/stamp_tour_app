package com.ljystamp.common.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.ljystamp.common.domain.respository.SaveTourLocationRepository
import com.ljystamp.stamp_tour_app.model.SaveResult
import com.ljystamp.stamp_tour_app.model.SavedLocation
import com.ljystamp.stamp_tour_app.model.TourMapper
import javax.inject.Inject

class SaveTourLocationRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
): SaveTourLocationRepository {
    override fun saveTourLocation(tour: TourMapper, onComplete: (SaveResult, SavedLocation?) -> Unit) {
        val userId = auth.currentUser?.uid ?: run {
            onComplete(SaveResult.LoginRequired("로그인이 필요해요"), null)
            return
        }

        // 현재 저장된 장소 수 확인
        db.collection("saved_locations")
            .whereEqualTo("userId", userId)
            .whereEqualTo("isStamped", false)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.size() >= 30) {
                    onComplete(SaveResult.MaxLimitReached("최대 30개까지 저장 가능해요"), null)
                    return@addOnSuccessListener
                }

                val savedLocationMap = hashMapOf(
                    "userId" to userId,
                    "contentId" to tour.contentId,
                    "contentTypeId" to tour.contentTypeId,
                    "title" to tour.title,
                    "address" to tour.addr1,
                    "image" to tour.firstImage,
                    "latitude" to tour.mapY,
                    "longitude" to tour.mapX,
                    "isStamped" to false,
                    "savedAt" to FieldValue.serverTimestamp()
                )

                val savedLocation = SavedLocation(
                    contentId = tour.contentId,
                    contentTypeId = tour.contentTypeId,
                    title = tour.title,
                    address = tour.addr1,
                    image = tour.firstImage,
                    latitude = tour.mapY,
                    longitude = tour.mapX,
                    isVisited = false,
                    savedAt = null
                )

                db.collection("saved_locations")
                    .document("${userId}_${tour.contentId}")
                    .set(savedLocationMap)
                    .addOnSuccessListener {
                        onComplete(SaveResult.Success("장소가 저장 되었어요"),savedLocation)
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firebase", "Error saving location", e)
                        onComplete(SaveResult.Failure("저장에 실패했어요"), null)
                    }
            }
            .addOnFailureListener {
                onComplete(SaveResult.Failure("오류가 발생했어요"), null)
            }
    }
}