package com.ljystamp.common.domain.respository

import com.ljystamp.common.data.dataSource.GetLocationNearTourListRemoteSource
import com.ljystamp.stamp_tour_app.model.TourMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLocationNearTourListRepositoryImpl @Inject constructor(
    private val remoteDataSource: GetLocationNearTourListRemoteSource
): GetLocationNearTourListRepository {
    override fun getLocationTourList(longitude: Double, latitude: Double, pageNo: Int, contentTypeId: Int): Flow<List<TourMapper>> {
        return flow {
            emit(remoteDataSource.getLocationNearTourList(longitude, latitude, pageNo, contentTypeId))
        }
    }
}