package com.ljystamp.stamp_tour_app.repository

import android.util.Log
import com.ljystamp.stamp_tour_app.api.ApiService
import com.ljystamp.stamp_tour_app.api.model.TourMapper
import com.ljystamp.stamp_tour_app.api.model.TourismResponse
import com.ljystamp.stamp_tour_app.api.model.toTourMapperList
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.retrofit.body
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocationTourListRepository(
    private val apiService: ApiService
) {
    fun getLocationTourList(longitude: Double, latitude: Double, contentTypeId: Int): Flow<List<TourMapper>> {
        val tourList = ArrayList<TourMapper>()
        return flow {
           apiService.getLocationTourList(
               longitude = longitude,
               latitude = latitude,
               contentTypeId = contentTypeId
           ).onSuccess {
               tourList.addAll(this.body.toTourMapperList())
           }.onFailure {
               Log.e("ljy", "fail")
           }
            emit(tourList)
        }
    }
}