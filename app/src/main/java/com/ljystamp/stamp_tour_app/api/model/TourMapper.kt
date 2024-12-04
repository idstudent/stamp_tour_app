package com.ljystamp.stamp_tour_app.api.model

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
    val contentid: String,
    val contenttypeid: String,
    val createdtime: String,
    val dist: String,
    val firstimage: String,
    val firstimage2: String,
    val cpyrhtDivCd: String,
    val mapx: String,
    val mapy: String,
    val mlevel: String,
    val modifiedtime: String,
    val sigungucode: String,
    val tel: String,
    val title: String
)

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
                contentid = item.contentid ?: "",
                contenttypeid = item.contenttypeid ?: "",
                createdtime = item.createdtime ?: "",
                dist = item.dist ?: "",
                firstimage = item.firstimage ?: "",
                firstimage2 = item.firstimage2 ?: "",
                cpyrhtDivCd = item.cpyrhtDivCd ?: "",
                mapx = item.mapx ?: "",
                mapy = item.mapy ?: "",
                mlevel = item.mlevel ?: "",
                modifiedtime = item.modifiedtime ?: "",
                sigungucode = item.sigungucode ?: "",
                tel = item.tel ?: "",
                title = item.title ?: ""
            )
        }
    }
}