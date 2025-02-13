package com.ljystamp.common.di

import com.ljystamp.common.domain.respository.GetLocationNearTourListRepository
import com.ljystamp.common.domain.respository.GetTourDetailRepository
import com.ljystamp.common.domain.respository.SaveTourLocationRepository
import com.ljystamp.common.domain.respository.SavedLocationRepository
import com.ljystamp.common.domain.usecase.GetLocationNearTourListUseCase
import com.ljystamp.common.domain.usecase.GetSavedLocationsUseCase
import com.ljystamp.common.domain.usecase.GetTourDetailUseCase
import com.ljystamp.common.domain.usecase.SaveTourLocationsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CommonUseCaseModule {
    @Singleton
    @Provides
    fun provideGetLocationNearTourListUseCase(getLocationNearTourListRepository: GetLocationNearTourListRepository) =
        GetLocationNearTourListUseCase(getLocationNearTourListRepository)

    @Singleton
    @Provides
    fun provideGetSavedLocationsUseCase(savedLocationRepository: SavedLocationRepository) =
        GetSavedLocationsUseCase(savedLocationRepository)

    @Singleton
    @Provides
    fun provideSaveTourLocationsUseCase(saveTourLocationRepository: SaveTourLocationRepository) =
        SaveTourLocationsUseCase(saveTourLocationRepository)

    @Singleton
    @Provides
    fun provideGetTourDetailUseCase(getTourDetailRepository: GetTourDetailRepository) =
        GetTourDetailUseCase(getTourDetailRepository)
}