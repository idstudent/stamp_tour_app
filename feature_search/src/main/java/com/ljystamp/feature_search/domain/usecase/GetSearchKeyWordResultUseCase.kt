package com.ljystamp.feature_search.domain.usecase

import com.ljystamp.feature_search.domain.repository.GetSearchKeyWordResultRepository
import com.ljystamp.stamp_tour_app.model.TourMapper
import javax.inject.Inject

class GetSearchKeyWordResultUseCase @Inject constructor(
    private val getSearchKeyWordRepository: GetSearchKeyWordResultRepository
){
    suspend operator fun invoke(keyword: String, contentTypeId: Int, page: Int): List<TourMapper> {
        return getSearchKeyWordRepository.getSearchKeyword(keyword, contentTypeId, page)
    }
}