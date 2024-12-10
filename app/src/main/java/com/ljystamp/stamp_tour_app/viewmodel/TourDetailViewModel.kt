package com.ljystamp.stamp_tour_app.viewmodel

import androidx.lifecycle.ViewModel
import com.ljystamp.stamp_tour_app.api.model.DetailItem
import com.ljystamp.stamp_tour_app.repository.TourDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class TourDetailViewModel @Inject constructor(
    private val tourDetailRepository: TourDetailRepository
): ViewModel() {
    fun getTourDetail(contentId: Int, contentTypeId: Int): Flow<List<DetailItem>> {
        return tourDetailRepository.getTourDetail(contentId, contentTypeId)
            .catch { it.printStackTrace() }
    }
}