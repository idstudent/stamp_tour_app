package com.ljystamp.feature_search.data.datasource

import com.ljystamp.stamp_tour_app.model.TourMapper

interface GetSearchKeyWordResultRemoteSource {
    suspend fun  getSearchKeyword(keyword: String, contentTypeId: Int, page: Int): List<TourMapper>
}