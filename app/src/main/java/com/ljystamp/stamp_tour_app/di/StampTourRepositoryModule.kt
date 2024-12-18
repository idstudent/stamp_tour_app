package com.ljystamp.stamp_tour_app.di

import com.ljystamp.stamp_tour_app.api.ApiService
import com.ljystamp.stamp_tour_app.repository.LocationTourListRepository
import com.ljystamp.stamp_tour_app.repository.SearchKeywordRepository
import com.ljystamp.stamp_tour_app.repository.TourDetailRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StampTourRepositoryModule {

    @Singleton
    @Provides
    fun provideLocationTourListRepository(apiService: ApiService): LocationTourListRepository {
        return LocationTourListRepository(apiService)
    }

    @Singleton
    @Provides
    fun provideTourDetailRepository(apiService: ApiService): TourDetailRepository {
        return TourDetailRepository(apiService)
    }

    @Singleton
    @Provides
    fun provideGetSearchKeywordRepository(apiService: ApiService): SearchKeywordRepository {
        return SearchKeywordRepository(apiService)
    }
}