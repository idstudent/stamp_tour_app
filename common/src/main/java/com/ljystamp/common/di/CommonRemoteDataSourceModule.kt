package com.ljystamp.common.di

import com.ljystamp.common.data.repository.GetLocationNearTourListRepositoryImpl
import com.ljystamp.common.domain.respository.GetLocationNearTourListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonRemoteDataSourceModule {
    @Singleton
    @Binds
    abstract fun bindGetLocationNearTourListRemoteDataSource(
        getLocationNearTourListRepositoryImpl: GetLocationNearTourListRepositoryImpl
    ): GetLocationNearTourListRepository
}