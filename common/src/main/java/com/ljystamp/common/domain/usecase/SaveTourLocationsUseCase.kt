package com.ljystamp.common.domain.usecase

import com.ljystamp.common.domain.respository.SaveTourLocationRepository
import com.ljystamp.stamp_tour_app.model.SaveResult
import com.ljystamp.stamp_tour_app.model.SavedLocation
import com.ljystamp.stamp_tour_app.model.TourMapper
import javax.inject.Inject

class SaveTourLocationsUseCase @Inject constructor(
    private val saveTourLocationRepository: SaveTourLocationRepository
){
    operator fun invoke(tour: TourMapper, onComplete: (SaveResult, SavedLocation?) -> Unit) {
        saveTourLocationRepository.saveTourLocation(tour, onComplete)
    }
}