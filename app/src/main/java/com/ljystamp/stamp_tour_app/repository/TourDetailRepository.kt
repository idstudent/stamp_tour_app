package com.ljystamp.stamp_tour_app.repository

import android.util.Log
import com.ljystamp.stamp_tour_app.api.ApiService
import com.ljystamp.stamp_tour_app.api.model.DetailItem
import com.ljystamp.stamp_tour_app.db.StampDatabase
import com.ljystamp.stamp_tour_app.model.TourMapper
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.retrofit.body
import kotlinx.coroutines.flow.Flow

class TourDetailRepository(
    private val apiService: ApiService,
    private val stampDatabase: StampDatabase
) {
    suspend fun getTourDetail(contentId: Int, contentTypeId: Int): List<DetailItem> {
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

    suspend fun insertSearchItem(item: TourMapper) {
        return stampDatabase.stampDao().insertItem(item)
    }

    fun selectAllSearchItem(): Flow<List<TourMapper>> {
        return stampDatabase.stampDao().selectItem()
    }

    suspend fun removeOldestAndSaveNew(newItem: TourMapper) {
        stampDatabase.stampDao().getOldestItem()?.let { oldest ->
            stampDatabase.stampDao().deleteItem(oldest)
        }
        stampDatabase.stampDao().insertItem(newItem)
    }
}