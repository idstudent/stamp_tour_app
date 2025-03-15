package com.ljystamp.common.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.ljystamp.common.domain.usecase.GetLocationNearTourListUseCase
import com.ljystamp.common.domain.usecase.GetSavedLocationsUseCase
import com.ljystamp.common.domain.usecase.SaveTourLocationsUseCase
import com.ljystamp.stamp_tour_app.model.SaveResult
import com.ljystamp.stamp_tour_app.model.SavedLocation
import com.ljystamp.stamp_tour_app.model.TourMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationTourListViewModel @Inject constructor(
    private val getSavedLocationsUseCase: GetSavedLocationsUseCase,
    private val getLocationNearTourListUseCase: GetLocationNearTourListUseCase,
    private val saveTourLocationsUseCase: SaveTourLocationsUseCase
): ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val _savedLocations = MutableStateFlow<List<SavedLocation>>(emptyList())
    val savedLocations: StateFlow<List<SavedLocation>> = _savedLocations.asStateFlow()

    private val _nearTourList = MutableStateFlow<List<TourMapper>>(emptyList())
    val nearTourList = _nearTourList.asStateFlow()

    private var snapshotListener: ListenerRegistration? = null

    // 이부분 필요한건지 체크
//    init {
//        auth.currentUser?.let {
//            startObservingSavedLocations()
//        }
//    }
    fun startObservingSavedLocations() {
        viewModelScope.launch {
            getSavedLocationsUseCase.invoke().collect {
                _savedLocations.value = it
            }
        }
    }
    fun getLocationTourList(longitude: Double, latitude: Double, pageNo: Int, contentTypeId: Int) {
        Log.e("ljy", "ViewModel - getLocationTourList 호출 - lon: $longitude, lat: $latitude, page: $pageNo")
        viewModelScope.launch {
            try {
                getLocationNearTourListUseCase.invoke(longitude,latitude,pageNo, contentTypeId).collect {
                    Log.e("ljy", "ViewModel - 데이터 수신 - page: $pageNo, 항목 수: ${it.size}")
                    _nearTourList.value = it
                }
            } catch (e: Exception) {
                Log.e("ljy", "ViewModel - 오류 발생: ${e.message}")
            }
        }
    }

    fun saveTourLocation(tour: TourMapper, onComplete: ((SaveResult) -> Unit)? = null) {
       saveTourLocationsUseCase(tour) { saveResult, savedLocation ->
           when(saveResult) {
               is SaveResult.Success -> {
                   savedLocation?.let {
                       _savedLocations.value = _savedLocations.value + it
                   }
               }
               else -> {}
           }
           onComplete?.invoke(saveResult)
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