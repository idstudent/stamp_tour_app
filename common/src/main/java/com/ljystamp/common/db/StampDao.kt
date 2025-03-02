package com.ljystamp.common.db

import androidx.room.*
import com.ljystamp.stamp_tour_app.model.TourMapper
import kotlinx.coroutines.flow.Flow

@Dao
interface StampDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: TourMapper)

    @Delete
    suspend fun deleteItem(item: TourMapper)

    @Query("SELECT * FROM stamp ORDER BY timestamp DESC")
    fun selectItem(): Flow<List<TourMapper>>

    @Query("SELECT * FROM stamp ORDER BY timestamp ASC LIMIT 1")
    suspend fun getOldestItem(): TourMapper?
}