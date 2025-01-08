package com.ljystamp.stamp_tour_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljystamp.stamp_tour_app.api.model.DetailItem
import com.ljystamp.stamp_tour_app.api.model.TourMapper
import com.ljystamp.stamp_tour_app.repository.TourDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TourDetailViewModel @Inject constructor(
    private val tourDetailRepository: TourDetailRepository
): ViewModel() {
    private val _tourDetailInfo = MutableStateFlow<List<DetailItem>>(emptyList())
    val tourDetailInfo: StateFlow<List<DetailItem>> = _tourDetailInfo.asStateFlow()

    fun getTourDetail(contentId: Int, contentTypeId: Int) {
        viewModelScope.launch {
            val result = tourDetailRepository.getTourDetail(contentId, contentTypeId)

            _tourDetailInfo.value = result
        }
    }

    fun insertItem(item: TourMapper) {
        viewModelScope.launch {
            val currentList = tourDetailRepository.selectAllSearchItem().first()
            val updatedItem = item.copy(timestamp = System.currentTimeMillis())

            if(currentList.size >= 10) {
                tourDetailRepository.removeOldestAndSaveNew(updatedItem)
            } else {
                tourDetailRepository.insertSearchItem(updatedItem)
            }
        }
    }
}