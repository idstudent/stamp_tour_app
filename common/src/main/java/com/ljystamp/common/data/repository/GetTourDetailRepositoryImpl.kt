package com.ljystamp.common.data.repository

import com.ljystamp.common.data.dataSource.GetTourDetailRemoteSource
import com.ljystamp.common.domain.respository.GetTourDetailRepository
import com.ljystamp.stamp_tour_app.model.DetailItem

class GetTourDetailRepositoryImpl(
    private val getTourDetailRemoteSource: GetTourDetailRemoteSource
): GetTourDetailRepository {
    override suspend fun getTourDetail(contentId: Int, contentTypeId: Int): List<DetailItem> {
        return getTourDetailRemoteSource.getTourDetail(contentId, contentTypeId)
    }
}