package com.ljystamp.common.domain.respository

import com.ljystamp.stamp_tour_app.model.TourMapper
import kotlinx.coroutines.flow.Flow

interface GetLocationNearTourListRepository {
    fun getLocationTourList(longitude: Double, latitude: Double, pageNo: Int, contentTypeId: Int): Flow<List<TourMapper>>
}