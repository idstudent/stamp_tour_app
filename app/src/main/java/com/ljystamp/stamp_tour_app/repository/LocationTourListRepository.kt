package com.ljystamp.stamp_tour_app.repository

import android.util.Log
import com.ljystamp.stamp_tour_app.api.ApiService
import com.ljystamp.stamp_tour_app.api.model.TourMapper
import com.ljystamp.stamp_tour_app.api.model.toTourMapperList
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.retrofit.body

class LocationTourListRepository(
    private val apiService: ApiService
) {
    suspend fun getLocationTourList(longitude: Double, latitude: Double, pageNo: Int, contentTypeId: Int): List<TourMapper> {
        val tourList = ArrayList<TourMapper>()

        apiService.getLocationTourList(
            longitude = longitude,
            latitude = latitude,
            pageNo = pageNo,
            contentTypeId = contentTypeId
        ).onSuccess {
            tourList.addAll(this.body.toTourMapperList())
        }.onFailure {
            Log.e("ljy", "fail")
        }

        return tourList
    }
}