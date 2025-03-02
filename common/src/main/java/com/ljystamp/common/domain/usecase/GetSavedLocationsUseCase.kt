package com.ljystamp.common.domain.usecase

import com.ljystamp.common.domain.respository.SavedLocationRepository
import com.ljystamp.stamp_tour_app.model.SavedLocation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedLocationsUseCase @Inject constructor(
    private val savedLocationRepository: SavedLocationRepository
){
    operator fun invoke(): Flow<List<SavedLocation>> {
        return savedLocationRepository.startObservingSavedLocations()
    }
}