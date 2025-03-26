package com.ljystamp.feature_near_place.presentation.view

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.ljystamp.common.presentation.viewmodel.LocationTourListViewModel
import com.ljystamp.core_navigation.AppRoutes
import com.ljystamp.core_ui.presentation.component.TourItem
import com.ljystamp.core_ui.theme.AppTypography
import com.ljystamp.stamp_tour_app.model.SaveResult
import com.ljystamp.stamp_tour_app.model.TourMapper
import java.net.URLEncoder
import com.google.gson.Gson

@Composable
fun NearPlaceListScreen(
    navController: NavController,
    locationTourListViewModel: LocationTourListViewModel,
    contentTypeId: Int
) {
    var page by remember { mutableStateOf(1) }
    var isLoading by remember { mutableStateOf(false) }

    val currentTourList = remember { mutableStateListOf<TourMapper>() }

    val context = LocalContext.current

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    fun getNearList() {
        if (isLoading) return
        isLoading = true

        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        locationTourListViewModel.getLocationTourList(
                            it.longitude,
                            it.latitude,
                            page,
                            contentTypeId
                        )
                    } ?: run {
                        isLoading = true
                        Toast.makeText(context, "위치 정보를 가져올 수 없어요.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    isLoading = true
                    Toast.makeText(context, "위치 정보 조회 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } catch (e: SecurityException) {
            isLoading = true
            Toast.makeText(context, "위치 권한이 없어요.", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(contentTypeId, page) {
        getNearList()
    }

    LaunchedEffect(Unit) {
        locationTourListViewModel.nearTourList.collect { newTourList ->

            if (page == 1) {
                currentTourList.clear()
            }

            if (newTourList.isEmpty()) {
                isLoading = true
            } else {
                currentTourList.addAll(newTourList)
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = when(contentTypeId) {
                12 -> "내 근처 여행지"
                14 -> "내 근처 문화 시설"
                15 -> "내 근처 축제"
                28 -> "내 근처 액티비티"
                39 -> "내 근처 식당"
                else -> ""
            },
            style = AppTypography.fontSize24ExtraBold,
            modifier = Modifier
                .padding(top = 32.dp, start = 20.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp, start = 8.dp, end = 8.dp)
        ){
            items(currentTourList.size) {index ->
                val item = currentTourList[index]
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
                                    navController.navigate(AppRoutes.LOGIN)
                                }
                            }
                        }
                    },
                    onItemClick = {
                        val gson = Gson()
                        val itemJson = gson.toJson(item)
                        val encodedItem = URLEncoder.encode(itemJson, "UTF-8")
                        navController.navigate("${AppRoutes.TOUR_DETAIL}/$encodedItem/${false}")
                    },
                    isSaved = isSaved.value
                )

                if (index >= currentTourList.size - 3 && !isLoading) {
                    page++
                    getNearList()
                }
            }
        }
    }
}
