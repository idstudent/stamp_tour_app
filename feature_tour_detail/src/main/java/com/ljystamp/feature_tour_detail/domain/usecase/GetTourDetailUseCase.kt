package com.ljystamp.feature_tour_detail.domain.usecase

import com.ljystamp.feature_tour_detail.domain.repository.GetTourDetailRepository
import com.ljystamp.stamp_tour_app.model.DetailItem
import javax.inject.Inject

class GetTourDetailUseCase @Inject constructor(
    private val getTourDetailRepository: GetTourDetailRepository
) {
    suspend operator fun invoke(contentId: Int, contentTypeId: Int): List<DetailItem>{
        return getTourDetailRepository.getTourDetail(contentId, contentTypeId)
    }
}