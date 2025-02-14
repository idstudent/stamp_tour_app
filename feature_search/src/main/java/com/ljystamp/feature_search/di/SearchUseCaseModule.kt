package com.ljystamp.feature_search.di

import com.ljystamp.feature_search.domain.repository.GetSearchKeyWordResultRepository
import com.ljystamp.feature_search.domain.usecase.GetSearchKeyWordResultUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SearchUseCaseModule {
    @Singleton
    @Provides
    fun provideGetSearchKeyWordResultUseCase(getSearchKeyWordResultRepository: GetSearchKeyWordResultRepository) =
        GetSearchKeyWordResultUseCase(getSearchKeyWordResultRepository)
}