package com.ljystamp.stamp_tour_app.db

import androidx.room.*
import com.ljystamp.stamp_tour_app.api.model.TourMapper
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