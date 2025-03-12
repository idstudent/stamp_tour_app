package com.ljystamp.feature_home.presentation.coponent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ljystamp.common.presentation.viewmodel.LocationTourListViewModel
import com.ljystamp.core_ui.presentation.component.NearItem
import com.ljystamp.stamp_tour_app.model.TourMapper

@Composable
fun HomeNearTourList(
    nearTourList: List<TourMapper>,
    locationTourListViewModel: LocationTourListViewModel
) {
    if(nearTourList.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 40.dp)
        ) {
            nearTourList.take(4).forEachIndexed { index, item ->
                NearItem(nearLocation = item) {

                }
                if(index < nearTourList.take(4).size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }else {
        HomeNearEmptyView()
    }
}