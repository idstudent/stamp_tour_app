package com.ljystamp.common.domain.respository

import com.ljystamp.stamp_tour_app.model.DetailItem

interface GetTourDetailRepository {
    suspend fun getTourDetail(contentId: Int, contentTypeId: Int): List<DetailItem>
}