package com.ljystamp.feature_search.domain.repository

import com.ljystamp.stamp_tour_app.model.TourMapper

interface GetSearchKeyWordResultRepository {
    suspend fun getSearchKeyword(keyword: String, contentTypeId: Int, page: Int): List<TourMapper>
}