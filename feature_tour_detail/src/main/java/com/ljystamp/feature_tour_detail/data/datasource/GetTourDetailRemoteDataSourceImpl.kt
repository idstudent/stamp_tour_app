package com.ljystamp.feature_tour_detail.data.datasource

import android.util.Log
import com.ljystamp.stamp_tour_app.api.ApiService
import com.ljystamp.stamp_tour_app.model.DetailItem
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.retrofit.body
import javax.inject.Inject

class GetTourDetailRemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
): GetTourDetailRemoteDataSource {
    override suspend fun getTourDetail(contentId: Int, contentTypeId: Int): List<DetailItem> {
        val detailInfo = ArrayList<DetailItem>()

        apiService.getTourDetail(contentId = contentId, contentTypeId = contentTypeId)
            .onSuccess {
                detailInfo.addAll(this.body.response.body.items.item)
            }
            .onFailure {
                Log.e("ljy", "detail error")
            }

        return detailInfo
    }
}