package com.ljystamp.feature_tour_detail.data.datasource

import com.ljystamp.common.db.StampDatabase
import com.ljystamp.stamp_tour_app.model.TourMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TourDetailLocalDataSourceImpl @Inject constructor(
    private val stampDatabase: StampDatabase
): TourDetailLocalDataSource{

    override suspend fun insertSearchItem(item: TourMapper) {
        return stampDatabase.stampDao().insertItem(item)
    }

    override fun selectAllSearchItem(): Flow<List<TourMapper>> {
        return stampDatabase.stampDao().selectItem()
    }

    override suspend fun removeOldestAndSaveNew(newItem: TourMapper) {
        stampDatabase.stampDao().getOldestItem()?.let { oldest ->
            stampDatabase.stampDao().deleteItem(oldest)
        }
        stampDatabase.stampDao().insertItem(newItem)
    }
}