package com.ljystamp.common.data.dataSource

import com.ljystamp.stamp_tour_app.model.TourMapper

interface GetLocationNearTourListRemoteDataSource {
    suspend fun getLocationNearTourList(longitude: Double, latitude: Double, pageNo: Int, contentTypeId: Int): List<TourMapper>
}