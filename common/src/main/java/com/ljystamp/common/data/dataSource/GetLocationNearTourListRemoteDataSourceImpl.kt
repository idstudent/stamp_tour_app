package com.ljystamp.common.data.dataSource

import android.util.Log
import com.ljystamp.stamp_tour_app.api.ApiService
import com.ljystamp.stamp_tour_app.model.TourMapper
import com.ljystamp.stamp_tour_app.model.toTourMapperList
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.retrofit.body
import javax.inject.Inject

class GetLocationNearTourListRemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
): GetLocationNearTourListRemoteDataSource {
    override suspend fun getLocationNearTourList(
        longitude: Double,
        latitude: Double,
        pageNo: Int,
        contentTypeId: Int
    ): List<TourMapper> {
        val tourList = ArrayList<TourMapper>()

        apiService.getLocationTourList(
            longitude = longitude,
            latitude = latitude,
            pageNo = pageNo,
            contentTypeId = contentTypeId
        ).onSuccess {
            this.body?.let {
                tourList.addAll(it.toTourMapperList())
            }
        }.onFailure {
            Log.e("ljy", "fail")
        }

        return tourList
    }
}