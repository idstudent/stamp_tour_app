package com.ljystamp.feature_tour_detail.data.datasource

import com.ljystamp.stamp_tour_app.model.DetailItem

interface GetTourDetailRemoteDataSource {
    suspend fun getTourDetail(contentId: Int, contentTypeId: Int): List<DetailItem>
}