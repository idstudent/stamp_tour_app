package com.ljystamp.feature_search.domain.usecase

import com.ljystamp.feature_tour_detail.domain.repository.TourDetailDBRepository
import com.ljystamp.stamp_tour_app.model.TourMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentlySearchUseCase @Inject constructor(
    private val tourDetailDBRepository: TourDetailDBRepository
){
    operator fun invoke(): Flow<List<TourMapper>> {
        return tourDetailDBRepository.selectAllSearchItem()
    }
}