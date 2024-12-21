package com.ljystamp.stamp_tour_app.api.model

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
    val totalCount: Int,
    val addr1: String,
    val addr2: String,
    val areacode: String,
    val booktour: String,
    val cat1: String,
    val cat2: String,
    val cat3: String,
    @ColumnInfo(defaultValue = "0")
    val timestamp: Long = 0,
    @PrimaryKey
    val contentid: Int,
    val contenttypeid: Int,
    val createdtime: String,
    val dist: String,
    val firstimage: String,
    val firstimage2: String,
    val cpyrhtDivCd: String,
    val mapx: Double,
    val mapy: Double,
    val mlevel: String,
    val modifiedtime: String,
    val sigungucode: String,
    val tel: String,
    val title: String
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
                areacode = item.areacode ?: "",
                booktour = item.booktour ?: "",
                cat1 = item.cat1 ?: "",
                cat2 = item.cat2 ?: "",
                cat3 = item.cat3 ?: "",
                contentid = item.contentid ?: -1,
                contenttypeid = item.contenttypeid ?: -1,
                createdtime = item.createdtime ?: "",
                dist = item.dist ?: "",
                firstimage = item.firstimage ?: "",
                firstimage2 = item.firstimage2 ?: "",
                cpyrhtDivCd = item.cpyrhtDivCd ?: "",
                mapx = item.mapx ?: 0.0,
                mapy = item.mapy ?: 0.0,
                mlevel = item.mlevel ?: "",
                modifiedtime = item.modifiedtime ?: "",
                sigungucode = item.sigungucode ?: "",
                tel = item.tel ?: "",
                title = item.title ?: ""
            )
        }
    }
}