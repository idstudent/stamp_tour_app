package com.ljystamp.stamp_tour_app.model

import com.google.gson.annotations.SerializedName

data class TourismResponse(
    val response: Response
)

data class Response(
    val header: Header,
    val body: Body
)

data class Header(
    val resultCode: String,
    val resultMsg: String
)

data class Body(
    val items: Items,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)

data class Items(
    val item: List<TourItem>
)

data class TourItem(
    val addr1: String?,
    val addr2: String?,
    @SerializedName("areacode")
    val areaCode: String?,
    @SerializedName("booktour")
    val bookTour: String?,
    val cat1: String?,
    val cat2: String?,
    val cat3: String?,
    @SerializedName("contentid")
    val contentId: Int?,
    @SerializedName("contenttypeid")
    val contentTypeId: Int?,
    @SerializedName("createdtime")
    val createdTime: String?,
    val dist: String?,
    @SerializedName("firstimage")
    val firstImage: String?,
    @SerializedName("firstimage2")
    val firstImage2: String?,
    val cpyrhtDivCd: String?,
    @SerializedName("mapx")
    val mapX: Double?,
    @SerializedName("mapy")
    val mapY: Double?,
    @SerializedName("mlevel")
    val level: String?,
    @SerializedName("modifiedtime")
    val modifiedTime: String?,
    @SerializedName("sigungucode")
    val sigunguCode: String?,
    val tel: String?,
    val title: String?
)