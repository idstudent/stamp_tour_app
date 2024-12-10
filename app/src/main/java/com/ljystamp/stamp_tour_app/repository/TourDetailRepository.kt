package com.ljystamp.stamp_tour_app.repository

import android.util.Log
import com.ljystamp.stamp_tour_app.api.ApiService
import com.ljystamp.stamp_tour_app.api.model.DetailItem
import com.ljystamp.stamp_tour_app.api.model.TourDetailResponse
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.retrofit.body
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TourDetailRepository(
    private val apiService: ApiService
) {
    fun getTourDetail(contentId: Int, contentTypeId: Int): Flow<List<DetailItem>> {
        val detailInfo = ArrayList<DetailItem>()

        return flow {
            apiService.getTourDetail(contentId = contentId, contentTypeId = contentTypeId)
                .onSuccess {
                    detailInfo.addAll(this.body.response.body.items.item)
                }
                .onFailure {
                    Log.e("ljy", "detail error")
                }
            emit(detailInfo)
        }
    }
}