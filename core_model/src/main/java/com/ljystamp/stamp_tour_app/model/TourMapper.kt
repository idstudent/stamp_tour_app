package com.ljystamp.stamp_tour_app.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "stamp")
data class TourMapper(
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int? = 0,
    val addr1: String? = "",
    val addr2: String? = "",
    val areaCode: String,
    @ColumnInfo(defaultValue = "0")
    val timestamp: Long = 0,
    @PrimaryKey
    val contentId: Int,
    val contentTypeId: Int,
    val createdTime: String,
    val dist: String,
    val firstImage: String? = "",
    val firstImage2: String? = "",
    val cpyrhtDivCd: String,
    val mapX: Double,
    val mapY: Double,
    val level: String,
    val modifiedTime: String? = "",
    val sigunguCode: String,
    val tel: String? = "",
    val title: String? = ""
) : Parcelable

fun TourismResponse.toTourMapperList(): List<TourMapper> {
    return this.response.body.let { body ->
        body.items.item.map { item ->
            TourMapper(
                numOfRows = body.numOfRows,
                pageNo = body.pageNo,
                totalCount = body.totalCount,
                addr1 = item.addr1 ?: "",
                addr2 = item.addr2 ?: "",
                areaCode = item.areaCode ?: "",
                contentId = item.contentId ?: -1,
                contentTypeId = item.contentTypeId ?: -1,
                createdTime = item.createdTime ?: "",
                dist = item.dist ?: "",
                firstImage = item.firstImage ?: "",
                firstImage2 = item.firstImage2 ?: "",
                cpyrhtDivCd = item.cpyrhtDivCd ?: "",
                mapX = item.mapX ?: 0.0,
                mapY = item.mapY ?: 0.0,
                level = item.level ?: "",
                modifiedTime = item.modifiedTime ?: "",
                sigunguCode = item.sigunguCode ?: "",
                tel = item.tel ?: "",
                title = item.title ?: ""
            )
        }
    }
}