package com.ljystamp.feature_my_tour.presentation.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.ljystamp.common.presentation.viewmodel.LocationTourListViewModel
import com.ljystamp.core_navigation.AppRoutes
import com.ljystamp.core_ui.theme.AppTypography
import com.ljystamp.feature_my_tour.presentation.component.MyTourItem
import java.net.URLEncoder

@Composable
fun MyTourListScreen(
    navController: NavController,
    locationTourListViewModel: LocationTourListViewModel
) {
    var isLocationPermissionGranted by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val tourList = locationTourListViewModel.savedLocations.collectAsState().value

    fun checkLocationPermission() {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED) {
                        isLocationPermissionGranted = true
                    } else {
                        isLocationPermissionGranted = false
                        Toast.makeText(
                            context,
                            "정확한 위치 확인을 위해 '정확한 위치' 권한을 허용해주세요",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onPermissionDenied(deniedPermissions: List<String>) {
                    isLocationPermissionGranted = false
                    Toast.makeText(
                        context,
                        "위치 권한이 거부되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
            .setDeniedMessage("정확한 위치 권한을 받지 않으면 몇몇 기능을 사용하지 못해요!\n정확한 위치를 켜시려면 설정 > 권한 > 위치 > 정확한 위치사용을 켜주세요")
            .setPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .check()
    }

    LaunchedEffect(Unit) {
        checkLocationPermission()
    }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "진행 중인 스탬프",
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
            items(tourList.size) {index ->
                val item = tourList[index]

                MyTourItem(
                    savedLocation = item,
                    onButtonClick = {
                        if(isLocationPermissionGranted) {
                            try {
                                fusedLocationClient.lastLocation
                                    .addOnSuccessListener { location ->
                                        location?.let {
                                            val results = FloatArray(1)
                                            Location.distanceBetween(
                                                it.latitude,
                                                it.longitude,
                                                item.latitude,
                                                item.longitude,
                                                results
                                            )

                                            val distanceInMeters = results[0]
                                            if (distanceInMeters <= 300) {
                                                locationTourListViewModel.updateVisitStatus(item.contentId) { _, message ->
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
                        val itemJson = gson.toJson(item)
                        val encodedItem = URLEncoder.encode(itemJson, "UTF-8")
                        navController.navigate("${AppRoutes.MY_TOUR_DETAIL}/$encodedItem/${false}")
                    })
            }
        }
    }
}