package com.ljystamp.feature_tour_detail.domain.usecase

import com.ljystamp.feature_tour_detail.domain.repository.TourDetailDBRepository
import com.ljystamp.stamp_tour_app.model.TourMapper
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SearchHistoryUseCase @Inject constructor(
    private val tourDetailDBRepository: TourDetailDBRepository
) {
    suspend operator fun invoke(item: TourMapper) {
        val currentList = tourDetailDBRepository.selectAllSearchItem().first()
        val updatedItem = item.copy(timestamp = System.currentTimeMillis())

        if(currentList.size >= 10) {
            tourDetailDBRepository.removeOldestAndSaveNew(updatedItem)
        } else {
            tourDetailDBRepository.insertSearchItem(updatedItem)
        }
    }
}