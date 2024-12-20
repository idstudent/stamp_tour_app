package com.ljystamp.stamp_tour_app.db

import androidx.room.*
import com.ljystamp.stamp_tour_app.api.model.TourMapper

@Dao
interface StampDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(item: TourMapper)

    @Delete
    suspend fun deleteBook(item: TourMapper)

    @Query("SELECT * FROM stamp")
    suspend fun selectBooks() : List<TourMapper>
}