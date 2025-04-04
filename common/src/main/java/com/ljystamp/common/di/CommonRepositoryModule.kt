package com.ljystamp.common.di

import com.ljystamp.common.data.dataSource.GetLocationNearTourListRemoteDataSource
import com.ljystamp.common.data.dataSource.GetLocationNearTourListRemoteDataSourceImpl
import com.ljystamp.common.data.repository.GetLocationNearTourListRepositoryImpl
import com.ljystamp.common.data.repository.SaveTourLocationRepositoryImpl
import com.ljystamp.common.data.repository.SavedLocationRepositoryImpl
import com.ljystamp.common.domain.respository.GetLocationNearTourListRepository
import com.ljystamp.common.domain.respository.SaveTourLocationRepository
import com.ljystamp.common.domain.respository.SavedLocationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindGetLocationNearTourListRepository(
        impl: GetLocationNearTourListRepositoryImpl
    ): GetLocationNearTourListRepository

    @Singleton
    @Binds
    abstract fun bindGetLocationNearTourListRemoteSource(
        impl: GetLocationNearTourListRemoteDataSourceImpl
    ): GetLocationNearTourListRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindSavedLocationRepository(
        impl: SavedLocationRepositoryImpl
    ): SavedLocationRepository

    @Singleton
    @Binds
    abstract fun bindSaveTourLocationRepository(
        impl: SaveTourLocationRepositoryImpl
    ): SaveTourLocationRepository
}