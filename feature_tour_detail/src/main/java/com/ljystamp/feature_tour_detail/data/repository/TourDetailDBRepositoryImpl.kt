package com.ljystamp.feature_tour_detail.data.repository

import com.ljystamp.feature_tour_detail.data.datasource.TourDetailLocalSource
import com.ljystamp.feature_tour_detail.domain.repository.TourDetailDBRepository
import com.ljystamp.stamp_tour_app.model.TourMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TourDetailDBRepositoryImpl @Inject constructor(
    private val tourDetailLocalSource: TourDetailLocalSource
): TourDetailDBRepository {
    override suspend fun insertSearchItem(item: TourMapper) {
        tourDetailLocalSource.insertSearchItem(item)
    }

    override fun selectAllSearchItem(): Flow<List<TourMapper>> {
        return tourDetailLocalSource.selectAllSearchItem()
    }

    override suspend fun removeOldestAndSaveNew(newItem: TourMapper) {
        tourDetailLocalSource.removeOldestAndSaveNew(newItem)
    }
}