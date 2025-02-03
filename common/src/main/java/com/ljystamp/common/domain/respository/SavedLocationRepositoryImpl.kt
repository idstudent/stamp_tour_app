package com.ljystamp.common.domain.respository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ljystamp.stamp_tour_app.model.SavedLocation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SavedLocationRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
): SavedLocationRepository {
    override fun startObservingSavedLocations(): Flow<List<SavedLocation>> {
        return callbackFlow {
            val userId = auth.currentUser?.uid ?: return@callbackFlow

            val snapshotListener = db.collection("saved_locations")
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

                        trySend(locations)
                    } catch (e: Exception) {
                        Log.e("Firebase", "Error parsing data: ${e.message}", e)
                    }
                }

            awaitClose { snapshotListener.remove() }
        }
    }
}