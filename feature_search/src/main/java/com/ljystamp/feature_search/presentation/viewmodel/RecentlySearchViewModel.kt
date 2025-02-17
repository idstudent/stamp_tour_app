package com.ljystamp.feature_search.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljystamp.feature_search.domain.usecase.GetRecentlySearchUseCase
import com.ljystamp.feature_tour_detail.domain.repository.TourDetailDBRepository
import com.ljystamp.stamp_tour_app.model.TourMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentlySearchViewModel @Inject constructor (
    private val getRecentlySearchUseCase: GetRecentlySearchUseCase
): ViewModel(){
    private val _recentlySearchResult = MutableStateFlow<List<TourMapper>>(emptyList())
    val recentlySearchResult: StateFlow<List<TourMapper>> = _recentlySearchResult.asStateFlow()
    fun selectRecentlySearchItem() {
        viewModelScope.launch {
            getRecentlySearchUseCase.invoke()
        }
    }
}