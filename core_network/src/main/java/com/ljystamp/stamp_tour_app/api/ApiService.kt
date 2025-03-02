package com.ljystamp.stamp_tour_app.api

import com.ljystamp.stamp_tour_app.model.TourDetailResponse
import com.ljystamp.stamp_tour_app.model.TourismResponse
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
        @Query("MobileOS") os: String = "AND",
        @Query("MobileApp") mobileOs: String = "TEST",
        @Query("_type") type: String = "json",
        @Query("arrange") arrange: String = "Q",
        @Query("mapX") longitude: Double,
        @Query("mapY") latitude: Double,
        @Query("radius") radiusInt: Int = 10000,
        @Query("contentTypeId") contentTypeId: Int
    ): ApiResponse<TourismResponse>

    @GET("detailIntro1")
    suspend fun getTourDetail(
        @Query("MobileOS") os: String = "AND",
        @Query("MobileApp") mobileOs: String = "TEST",
        @Query("_type") type: String = "json",
        @Query("contentId") contentId: Int,
        @Query("contentTypeId") contentTypeId: Int,
        @Query("numOfRows") pageResultCount: Int = 20,
        @Query("pageNo") pageNo: Int = 1
    ): ApiResponse<TourDetailResponse>

    @GET("searchKeyword1")
    suspend fun getSearchKeyword(
        @Query("numOfRows") pageResultCount: Int = 20,
        @Query("pageNo") pageNo: Int = 1,
        @Query("MobileOS") os: String = "AND",
        @Query("MobileApp") mobileOs: String = "TEST",
        @Query("_type") type: String = "json",
        @Query("listYN") listYN: String = "Y",
        @Query("arrange") arrange: String = "C",
        @Query("keyword") keyword: String,
        @Query("contentTypeId") contentTypeId: Int
    ): ApiResponse<TourismResponse>
}