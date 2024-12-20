package com.ljystamp.stamp_tour_app.di

import android.app.Application
import androidx.room.Room
import com.ljystamp.stamp_tour_app.db.StampDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideBookMarkDatabase(app : Application) =
        Room.databaseBuilder(app, StampDatabase::class.java, "stamp_db")
            .fallbackToDestructiveMigration()
            .build()
}