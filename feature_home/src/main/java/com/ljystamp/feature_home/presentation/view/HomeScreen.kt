package com.ljystamp.feature_home.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.LocationServices
import com.ljystamp.common.presentation.viewmodel.LocationTourListViewModel
import com.ljystamp.core_ui.theme.AppColors
import com.ljystamp.core_ui.theme.AppTypography
import com.ljystamp.feature_home.presentation.coponent.StampTourViewPager


@Composable
fun HomeScreen() {
    val locationTourListViewModel: LocationTourListViewModel = hiltViewModel()
    val savedLocations = locationTourListViewModel.savedLocations.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val context = LocalContext.current

        val notVisitedLocations = savedLocations.value.filter { !it.isVisited }
        val fusedLocationClient = remember {
            LocationServices.getFusedLocationProviderClient(context)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "진행 중인 스탬프",
                style = AppTypography.fontSize24ExtraBold,
            )

            if(notVisitedLocations.size > 5) {
                Text(
                    text = "더보기",
                    style = AppTypography.fontSize14Regular,
                )
            }
        }
        Spacer(modifier = Modifier.padding(top = 20.dp))

        StampTourViewPager(
            savedLocations = savedLocations.value,
            isLocationPermissionGranted = true, // 수정예정
            fusedLocationClient = fusedLocationClient,
            locationTourListViewModel = locationTourListViewModel,
            context = context
        )
    }
}