package com.ljystamp.feature_tour_detail.data.repository

import com.ljystamp.feature_tour_detail.data.datasource.GetTourDetailRemoteDataSource
import com.ljystamp.feature_tour_detail.domain.repository.GetTourDetailRepository
import com.ljystamp.stamp_tour_app.model.DetailItem

class GetTourDetailRepositoryImpl(
    private val getTourDetailRemoteSource: GetTourDetailRemoteDataSource
): GetTourDetailRepository {
    override suspend fun getTourDetail(contentId: Int, contentTypeId: Int): List<DetailItem> {
        return getTourDetailRemoteSource.getTourDetail(contentId, contentTypeId)
    }
}