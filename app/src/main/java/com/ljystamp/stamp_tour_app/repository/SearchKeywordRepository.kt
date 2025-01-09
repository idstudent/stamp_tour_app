package com.ljystamp.stamp_tour_app.repository

import android.util.Log
import com.ljystamp.stamp_tour_app.api.ApiService
import com.ljystamp.stamp_tour_app.api.model.TourMapper
import com.ljystamp.stamp_tour_app.api.model.toTourMapperList
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.retrofit.body
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchKeywordRepository(
    private val apiService: ApiService
) {
    fun getSearchKeyword(keyword: String, contentTypeId: Int, page: Int): Flow<List<TourMapper>> {
        val searchResult = ArrayList<TourMapper>()

        return flow {
            apiService.getSearchKeyword(keyword = keyword,contentTypeId = contentTypeId, pageNo = page)
                .onSuccess {
                    searchResult.addAll(this.body.toTourMapperList())
                }.onFailure {
                    Log.e("ljy", "search fail")
                }

            emit(searchResult)
        }
    }
}