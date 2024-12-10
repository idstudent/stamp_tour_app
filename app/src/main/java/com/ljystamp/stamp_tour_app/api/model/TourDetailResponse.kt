package com.ljystamp.stamp_tour_app.api.model

import com.google.gson.annotations.SerializedName

data class TourDetailResponse(
    val response: DetailResponse
)

data class DetailResponse(
    val header: DetailHeader,
    val body: DetailBody
)

data class DetailHeader(
    val resultCode: String,
    val resultMsg: String
)

data class DetailBody(
    val items: DetailItems
)

data class DetailItems(
    val item: List<DetailItem>
)

data class DetailItem(
    @SerializedName("contentid")
    val contentId: String,
    @SerializedName("contenttypeid")
    val contentTypeId: String,
    @SerializedName("opendate")
    val openDate: String,
    @SerializedName("restdate")
    val restDate: String,
    @SerializedName("usetime")
    val useTime: String
)