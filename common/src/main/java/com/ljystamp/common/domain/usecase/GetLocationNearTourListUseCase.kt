package com.ljystamp.common.domain.usecase

import com.ljystamp.common.domain.respository.GetLocationNearTourListRepository
import com.ljystamp.stamp_tour_app.model.TourMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocationNearTourListUseCase @Inject constructor(
    private val getLocationNearTourListRepository: GetLocationNearTourListRepository
){
    operator fun invoke(longitude: Double, latitude: Double, pageNo: Int, contentTypeId: Int): Flow<List<TourMapper>> {
        return getLocationNearTourListRepository.getLocationTourList(longitude, latitude, pageNo, contentTypeId)
    }
}