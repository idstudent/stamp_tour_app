package com.ljystamp.stamp_tour_app.viewmodel

import androidx.lifecycle.ViewModel
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
    fun getLocationTourList(longitude: Double, latitude: Double, contentTypeId: Int): Flow<List<TourMapper>> {
        return locationTourListRepository.getLocationTourList(longitude, latitude, contentTypeId)
            .catch { it.printStackTrace() }
    }
}