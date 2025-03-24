package com.ljystamp.feature_tour_detail.data.datasource

import com.ljystamp.stamp_tour_app.model.TourMapper
import kotlinx.coroutines.flow.Flow

interface TourDetailLocalDataSource {
    suspend fun insertSearchItem(item: TourMapper)
    fun selectAllSearchItem(): Flow<List<TourMapper>>
    suspend fun removeOldestAndSaveNew(newItem: TourMapper)

    suspend fun removeAllItem()
}