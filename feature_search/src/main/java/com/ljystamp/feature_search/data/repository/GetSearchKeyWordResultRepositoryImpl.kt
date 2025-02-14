package com.ljystamp.feature_search.data.repository

import com.ljystamp.feature_search.data.datasource.GetSearchKeyWordResultRemoteSource
import com.ljystamp.feature_search.domain.repository.GetSearchKeyWordResultRepository
import com.ljystamp.stamp_tour_app.api.ApiService
import com.ljystamp.stamp_tour_app.model.TourMapper

class GetSearchKeyWordResultRepositoryImpl(
    private val getSearchKeyWordResultRemoteSource: GetSearchKeyWordResultRemoteSource
): GetSearchKeyWordResultRepository {
    override suspend fun getSearchKeyword(keyword: String, contentTypeId: Int, page: Int): List<TourMapper> {
        return getSearchKeyWordResultRemoteSource.getSearchKeyword(keyword, contentTypeId, page)
    }
}