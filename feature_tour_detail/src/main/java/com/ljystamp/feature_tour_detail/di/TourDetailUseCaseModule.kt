package com.ljystamp.feature_tour_detail.di

import com.ljystamp.feature_tour_detail.domain.repository.GetTourDetailRepository
import com.ljystamp.feature_tour_detail.domain.repository.TourDetailDBRepository
import com.ljystamp.feature_tour_detail.domain.usecase.GetTourDetailUseCase
import com.ljystamp.feature_tour_detail.domain.usecase.SearchHistoryUseCase
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

    @Singleton
    @Provides
    fun provideSearchHistoryUseCase(tourDetailDBRepository: TourDetailDBRepository) =
        SearchHistoryUseCase(tourDetailDBRepository)
}