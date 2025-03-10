package com.ljystamp.common.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ljystamp.stamp_tour_app.model.TourMapper

@Database(entities = [TourMapper::class], version = 2)
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