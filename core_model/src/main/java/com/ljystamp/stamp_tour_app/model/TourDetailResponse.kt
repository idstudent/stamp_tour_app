package com.ljystamp.stamp_tour_app.model

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

    //여행지
    @SerializedName("opendate")
    val openDate: String?,
    @SerializedName("restdate")
    val restDate: String?,
    @SerializedName("usetime")
    val useTime: String?,

    //문화시설
    @SerializedName("usefee")
    val culturePrice: String?,
    @SerializedName("infocenterculture")
    val cultureInfoCenter: String,
    @SerializedName("usetimeculture")
    val cultureUseTime: String?,
    @SerializedName("restdateculture")
    val cultureRestDate: String?,
    @SerializedName("parkingculture")
    val cultureParking: String?,
    @SerializedName("parkingfee")
    val cultureParkingFee: String?,

    //축제
    @SerializedName("eventstartdate")
    val eventStartDate: String?,
    @SerializedName("eventenddate")
    val eventEndDate: String?,
    @SerializedName("playtime")
    val eventPlayTime: String?,
    @SerializedName("eventplace")
    val eventPlace: String?,
    @SerializedName("usetimefestival")
    val eventUsePrice: String?,
    @SerializedName("sponsor1")
    val eventSponsor: String?,
    @SerializedName("sponsor1tel")
    val eventSponsorTel: String?,

    //액티비티
    @SerializedName("reservation")
    val activityReservation: String?,
    @SerializedName("infocenterleports")
    val activityInfoCenter: String?,
    @SerializedName("restdateleports")
    val activityRestDate: String?,
    @SerializedName("usetimeleports")
    val activityUseTime: String?,
    @SerializedName("expagerangeleports")
    val activityPossibleAge: String?,
    @SerializedName("parkingleports")
    val activityParking: String?,

    //음식
    @SerializedName("firstmenu")
    val foodFirstMenu: String?,
    @SerializedName("treatmenu")
    val foodTreatMenu: String?,
    @SerializedName("infocenterfood")
    val foodInfoCenter: String?,
    @SerializedName("packing")
    val foodTakeOut: String?,
    @SerializedName("opentimefood")
    val foodOpenTime: String?,
    @SerializedName("restdatefood")
    val foodRestTime: String?,
)