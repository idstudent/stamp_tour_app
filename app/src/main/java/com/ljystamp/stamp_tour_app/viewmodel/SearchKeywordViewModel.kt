package com.ljystamp.stamp_tour_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljystamp.stamp_tour_app.api.model.TourMapper
import com.ljystamp.stamp_tour_app.repository.SearchKeywordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchKeywordViewModel @Inject constructor(
    private val searchKeywordRepository: SearchKeywordRepository
): ViewModel() {
    private val _getSearchResult = MutableStateFlow<List<TourMapper>>(emptyList())
    val getSearchResult = _getSearchResult.asStateFlow()

    suspend fun getSearchKeywordResult(keyword: String, contentTypeId: Int, page: Int) {
        viewModelScope.launch {
            val result = searchKeywordRepository.getSearchKeyword(keyword, contentTypeId, page)

            _getSearchResult.value = result
        }
    }
}