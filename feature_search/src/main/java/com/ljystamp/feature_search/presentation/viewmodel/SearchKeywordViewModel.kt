package com.ljystamp.feature_search.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljystamp.feature_search.domain.usecase.GetSearchKeyWordResultUseCase
import com.ljystamp.stamp_tour_app.model.TourMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchKeywordViewModel @Inject constructor(
    private val getSearchKeyWordResultUseCase: GetSearchKeyWordResultUseCase
): ViewModel() {
    private val _getSearchKeyWordResult = MutableStateFlow<List<TourMapper>>(emptyList())
    val getSearchKeyWordResult: StateFlow<List<TourMapper>> = _getSearchKeyWordResult.asStateFlow()

    fun getSearchKeywordResult(keyword: String, contentTypeId: Int, page: Int) {
        viewModelScope.launch {
            val result = getSearchKeyWordResultUseCase.invoke(keyword, contentTypeId, page)
            _getSearchKeyWordResult.value = result
        }
    }
}