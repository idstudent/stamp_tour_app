package com.ljystamp.common.domain.usecase

import com.ljystamp.common.domain.respository.GetTourDetailRepository
import com.ljystamp.stamp_tour_app.model.DetailItem
import javax.inject.Inject

class GetTourDetailUseCase @Inject constructor(
    private val getTourDetailRepository: GetTourDetailRepository
) {
    suspend operator fun invoke(contentId: Int, contentTypeId: Int): List<DetailItem>{
        return getTourDetailRepository.getTourDetail(contentId, contentTypeId)
    }
}