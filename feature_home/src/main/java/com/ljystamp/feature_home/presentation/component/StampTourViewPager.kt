package com.ljystamp.feature_home.presentation.component

import android.content.Context
import android.location.Location
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.gson.Gson
import com.ljystamp.common.presentation.viewmodel.LocationTourListViewModel
import com.ljystamp.core_navigation.AppRoutes
import com.ljystamp.core_ui.theme.AppColors
import com.ljystamp.stamp_tour_app.model.SavedLocation
import java.net.URLEncoder

@Composable
fun StampTourViewPager(
    navController: NavController,
    savedLocations: List<SavedLocation>,
    isLocationPermissionGranted: Boolean,
    fusedLocationClient: FusedLocationProviderClient,
    locationTourListViewModel: LocationTourListViewModel,
    context: Context
) {
    val notVisitedLocations = savedLocations.filter { !it.isVisited }.take(5)

    if(notVisitedLocations.isNotEmpty()) {
        Column {
            val pagerState = rememberPagerState { notVisitedLocations.size }

            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 20.dp),
                pageSpacing = 20.dp, // 아이템간 간격
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {page ->
                StampViewPagerItem(
                    savedLocation = notVisitedLocations[page],
                    onClick =  {
                        if(isLocationPermissionGranted) {
                            try {
                                fusedLocationClient.lastLocation
                                    .addOnSuccessListener { location ->
                                        location?.let {
                                            val results = FloatArray(1)
                                            Location.distanceBetween(
                                                it.latitude,
                                                it.longitude,
                                                notVisitedLocations[page].latitude,
                                                notVisitedLocations[page].longitude,
                                                results
                                            )

                                            val distanceInMeters = results[0]
                                            if (distanceInMeters <= 300) {
                                                locationTourListViewModel.updateVisitStatus(notVisitedLocations[page].contentId) { _, message ->
                                                    message?.let { msg ->
                                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            } else {
                                                Toast.makeText(context, "해당 장소와의 거리가 너무 멀어요! (${String.format("%.1f", distanceInMeters)}m)", Toast.LENGTH_SHORT).show()
                                            }
                                        } ?: run {
                                            Toast.makeText(context, "위치 정보를 가져올 수 없어요.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } catch (e: SecurityException) {
                                Toast.makeText(
                                    context,
                                    "위치 권한이 없습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }else {
                            Toast.makeText(context, "위치 권한이 필요해요.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onItemClick = {
                        val gson = Gson()
                        val itemJson = gson.toJson(notVisitedLocations[page])
                        val encodedItem = URLEncoder.encode(itemJson, "UTF-8")
                        navController.navigate("${AppRoutes.MY_TOUR_DETAIL}/$encodedItem/${false}")
                    }
                )
            }
            Spacer(modifier = Modifier.padding(top = 8.dp))

            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            ) {
                repeat(notVisitedLocations.size) { index ->
                    val isSelected = pagerState.currentPage == index

                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .padding(2.dp)
                            .border(
                                width = 1.dp,
                                color = AppColors.ColorFF8C00,
                                shape = CircleShape
                            )
                            .then(
                                if (isSelected) {
                                    Modifier.background(
                                        color = AppColors.ColorFF8C00,
                                        shape = CircleShape
                                    )
                                } else {
                                    Modifier
                                }
                            )
                    )

                    if (index < notVisitedLocations.size - 1) {
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                }
            }
        }
    }else {
        HomeStampEmptyView()
    }
}