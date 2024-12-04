package com.ljystamp.stamp_tour_app.di

import com.ljystamp.stamp_tour_app.api.ApiService
import com.ljystamp.stamp_tour_app.repository.LocationTourListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocationTourListRepositoryModule {
    @Singleton
    @Provides
    fun provideLocationTourListRepository(apiService: ApiService): LocationTourListRepository {
        return LocationTourListRepository(apiService)
    }
}