package com.ljystamp.feature_tour_detail.di

import com.ljystamp.feature_tour_detail.data.repository.GetTourDetailRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TourDetailRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindGetTourDetailRepository(
        impl: GetTourDetailRepositoryImpl
    ): GetTourDetailRepository
}