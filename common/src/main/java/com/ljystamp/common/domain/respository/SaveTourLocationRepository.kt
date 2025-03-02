package com.ljystamp.common.domain.respository

import com.ljystamp.stamp_tour_app.model.SaveResult
import com.ljystamp.stamp_tour_app.model.SavedLocation
import com.ljystamp.stamp_tour_app.model.TourMapper

interface SaveTourLocationRepository {
    fun saveTourLocation(tour: TourMapper, onComplete: (SaveResult, SavedLocation?) -> Unit)
}