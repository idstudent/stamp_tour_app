package com.ljystamp.stamp_tour_app.viewmodel

import androidx.lifecycle.ViewModel
import com.ljystamp.stamp_tour_app.api.model.TourMapper
import com.ljystamp.stamp_tour_app.repository.SearchKeywordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class SearchKeywordViewModel @Inject constructor(
    private val searchKeywordRepository: SearchKeywordRepository
): ViewModel() {
    fun getSearchKeywordResult(keyword: String, contentTypeId: Int, page: Int): Flow<List<TourMapper>> {
        return searchKeywordRepository.getSearchKeyword(keyword, contentTypeId, page)
            .catch { it.printStackTrace() }
    }
}