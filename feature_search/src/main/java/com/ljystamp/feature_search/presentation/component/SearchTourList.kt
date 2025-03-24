package com.ljystamp.feature_search.presentation.component

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.ljystamp.common.presentation.viewmodel.LocationTourListViewModel
import com.ljystamp.core_navigation.AppRoutes
import com.ljystamp.core_ui.presentation.component.NearEmptyView
import com.ljystamp.core_ui.presentation.component.TourItem
import com.ljystamp.feature_search.presentation.viewmodel.RecentlySearchViewModel
import com.ljystamp.stamp_tour_app.model.SaveResult
import com.ljystamp.stamp_tour_app.model.TourMapper
import java.net.URLEncoder

@Composable
fun SearchTourList(
    navController: NavController,
    tourList: List<TourMapper>,
    locationTourListViewModel: LocationTourListViewModel,
    emptyString: String
) {
    val context = LocalContext.current
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemWidth = screenWidth - 40.dp

    val handleLoginRequest = {
        navController.navigate(AppRoutes.LOGIN)
    }

    if(tourList.isNotEmpty()) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, bottom = 48.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tourList.take(4)) { item ->
                val isSaved = remember { mutableStateOf(false) }

                LaunchedEffect(item.contentId) {
                    locationTourListViewModel.checkIfLocationSaved(item.contentId) {
                        isSaved.value = it
                    }
                }

                TourItem(
                    nearLocation = item,
                    onButtonClick = {
                        locationTourListViewModel.saveTourLocation(item) { result ->
                            when(result) {
                                is SaveResult.Success -> {
                                    isSaved.value = true
                                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                                }
                                is SaveResult.Failure -> {
                                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                                }
                                is SaveResult.MaxLimitReached -> {
                                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                                }
                                is SaveResult.LoginRequired -> {
                                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                                    handleLoginRequest()
                                }
                            }
                        }
                    },
                    onItemClick = {
                        val gson = Gson()
                        val itemJson = gson.toJson(item)
                        val encodedItem = URLEncoder.encode(itemJson, "UTF-8")
                        navController.navigate("${AppRoutes.TOUR_DETAIL}/$encodedItem/${true}")
                    },
                    isSaved = isSaved.value,
                    modifier = Modifier.width(itemWidth)
                )
            }
        }
    } else {
        NearEmptyView(
            height = 160,
            icon = null,
            content = emptyString
        )
    }
}