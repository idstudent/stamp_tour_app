package com.ljystamp.feature_tour_detail.di

import com.ljystamp.feature_tour_detail.data.datasource.GetTourDetailRemoteDataSource
import com.ljystamp.feature_tour_detail.data.datasource.GetTourDetailRemoteDataSourceImpl
import com.ljystamp.feature_tour_detail.data.datasource.TourDetailLocalDataSource
import com.ljystamp.feature_tour_detail.data.datasource.TourDetailLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TourDetailDataSourceModule {
    @Singleton
    @Binds
    abstract fun bindGetTourDetailRemoteSource(
        getTourDetailRemoteSourceImpl: GetTourDetailRemoteDataSourceImpl
    ): GetTourDetailRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindTourDetailLocalSource(
        tourDetailLocalSourceImpl: TourDetailLocalDataSourceImpl
    ): TourDetailLocalDataSource
}