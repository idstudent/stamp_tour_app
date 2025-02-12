package com.ljystamp.common.data.dataSource

import android.util.Log
import com.ljystamp.stamp_tour_app.api.ApiService
import com.ljystamp.stamp_tour_app.model.DetailItem
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.retrofit.body
import javax.inject.Inject

class GetTourDetailRemoteSourceImpl @Inject constructor(
    private val apiService: ApiService
): GetTourDetailRemoteSource {
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