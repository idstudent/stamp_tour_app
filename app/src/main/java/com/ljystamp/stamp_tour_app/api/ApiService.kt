package com.ljystamp.stamp_tour_app.api

import com.ljystamp.stamp_tour_app.api.model.TourismResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    companion object {
        val BASE_URL = "https://apis.data.go.kr/B551011/KorService1/"
    }

    @GET("locationBasedList1")
    suspend fun getLocationTourList(
        @Query("numOfRows") pageResultCount: Int = 20,
        @Query("pageNo") pageNo: Int = 1,
        @Query("MobileOS") os: String = "json",
        @Query("MobileApp") mobileOs: String = "AND",
        @Query("_type") type: String = "json",
        @Query("arrange") arrange: String = "Q",
        @Query("mapX") longitude: Double,
        @Query("mapY") latitude: Double,
        @Query("radius") radiusInt: Int = 10000,
        @Query("contentTypeId") contentTypeId: Int,
        @Query("serviceKey") serviceKey: String = "qsfXiCfrr3NiyDSpe22tTnxtkMG3SPGz/j25Hxc5YHq9BvUpDPeT225KSHs6+VaaAxrMZnkNpYgVJ2g9BLfNsA=="
    ) : ApiResponse<TourismResponse>

}