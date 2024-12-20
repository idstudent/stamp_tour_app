package com.ljystamp.stamp_tour_app.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ljystamp.stamp_tour_app.api.model.TourMapper

@Database(entities = [TourMapper::class], version = 1)
abstract class StampDatabase  : RoomDatabase(){
    abstract fun stampDao() : StampDao

    companion object {
        fun newInstance(context : Context) : StampDatabase {
            return Room.databaseBuilder(context, StampDatabase::class.java, "stamp_db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}