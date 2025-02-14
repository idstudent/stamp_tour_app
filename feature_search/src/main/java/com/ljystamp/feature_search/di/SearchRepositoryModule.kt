package com.ljystamp.feature_search.di

import com.ljystamp.feature_search.data.datasource.GetSearchKeyWordResultRemoteSource
import com.ljystamp.feature_search.data.datasource.GetSearchKeyWordResultRemoteSourceImpl
import com.ljystamp.feature_search.data.repository.GetSearchKeyWordResultRepositoryImpl
import com.ljystamp.feature_search.domain.repository.GetSearchKeyWordResultRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindGetSearchKeyWordResultRepository(
        impl: GetSearchKeyWordResultRepositoryImpl
    ): GetSearchKeyWordResultRepository

    @Singleton
    @Binds
    abstract fun bindGetSearchKeyWordRemoteSource(
        impl: GetSearchKeyWordResultRemoteSourceImpl
    ): GetSearchKeyWordResultRemoteSource
}