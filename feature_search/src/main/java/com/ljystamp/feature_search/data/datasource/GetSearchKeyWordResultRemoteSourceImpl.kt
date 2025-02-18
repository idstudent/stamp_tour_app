package com.ljystamp.feature_search.data.datasource

import android.util.Log
import com.ljystamp.stamp_tour_app.api.ApiService
import com.ljystamp.stamp_tour_app.model.TourMapper
import com.ljystamp.stamp_tour_app.model.toTourMapperList
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.retrofit.body
import javax.inject.Inject

class GetSearchKeyWordResultRemoteSourceImpl @Inject constructor(
    private val apiService: ApiService
) : GetSearchKeyWordResultRemoteSource {
    override suspend fun getSearchKeyword(keyword: String, contentTypeId: Int, page: Int): List<TourMapper> {
        val searchResult = ArrayList<TourMapper>()

        apiService.getSearchKeyword(
            keyword = keyword,
            contentTypeId = contentTypeId,
            pageNo = page
        ).onSuccess {
            searchResult.addAll(this.body.toTourMapperList())
        }.onFailure {
            Log.e("ljy", "search fail")
        }

        return searchResult
    }
}