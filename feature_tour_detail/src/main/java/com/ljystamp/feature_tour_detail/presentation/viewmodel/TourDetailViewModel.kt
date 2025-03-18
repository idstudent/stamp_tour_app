package com.ljystamp.feature_tour_detail.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljystamp.feature_tour_detail.domain.usecase.GetTourDetailUseCase
import com.ljystamp.feature_tour_detail.domain.usecase.SearchHistoryUseCase
import com.ljystamp.stamp_tour_app.model.DetailItem
import com.ljystamp.stamp_tour_app.model.TourMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TourDetailViewModel @Inject constructor(
    private val getTourDetailUseCase: GetTourDetailUseCase,
    private val searchHistoryUseCase: SearchHistoryUseCase

): ViewModel() {
    private val _tourDetailInfo = MutableStateFlow<List<DetailItem>>(emptyList())
    val tourDetailInfo = _tourDetailInfo.asStateFlow()

    fun getTourDetail(contentId: Int, contentTypeId: Int) {
        viewModelScope.launch {
            val result = getTourDetailUseCase.invoke(contentId, contentTypeId)

            _tourDetailInfo.value = result
        }
    }

    fun insertItem(item: TourMapper) {
        viewModelScope.launch {
            searchHistoryUseCase.invoke(item)
        }
    }
}