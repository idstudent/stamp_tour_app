package com.ljystamp.feature_tour_detail.di

import com.ljystamp.feature_tour_detail.domain.usecase.GetTourDetailUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TourDetailUseCaseModule {
    @Singleton
    @Provides
    fun provideGetTourDetailUseCase(getTourDetailRepository: GetTourDetailRepository) =
        GetTourDetailUseCase(getTourDetailRepository)
}