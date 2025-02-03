package com.ljystamp.common.domain.respository

import com.ljystamp.stamp_tour_app.model.SavedLocation
import kotlinx.coroutines.flow.Flow

interface SavedLocationRepository {
    fun startObservingSavedLocations(): Flow<List<SavedLocation>>
}