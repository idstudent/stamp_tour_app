package com.ljystamp.common.data.dataSource

import com.ljystamp.stamp_tour_app.model.DetailItem

interface GetTourDetailRemoteSource {
    suspend fun getTourDetail(contentId: Int, contentTypeId: Int): List<DetailItem>
}