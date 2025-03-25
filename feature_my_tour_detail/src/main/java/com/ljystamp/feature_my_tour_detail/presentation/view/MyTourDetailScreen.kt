package com.ljystamp.feature_my_tour_detail.presentation.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.ljystamp.common.presentation.viewmodel.LocationTourListViewModel
import com.ljystamp.core_ui.theme.AppColors
import com.ljystamp.core_ui.theme.AppTypography
import com.ljystamp.feature_tour_detail.presentation.component.TourDetailContent
import com.ljystamp.feature_tour_detail.presentation.viewmodel.TourDetailViewModel
import com.ljystamp.stamp_tour_app.model.SavedLocation

@Composable
fun MyTourDetailScreen(
    navController: NavController,
    tourDetailViewModel: TourDetailViewModel,
    locationTourListViewModel: LocationTourListViewModel,
    tourInfo: SavedLocation?,
) {
    var isLocationPermissionGranted by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val detailInfo = tourDetailViewModel.tourDetailInfo.collectAsState().value

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


    LaunchedEffect(Unit) {
        tourInfo?.let {
            tourDetailViewModel.getTourDetail(tourInfo.contentId, tourInfo.contentTypeId)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 88.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Log.e("ljy", "상세 $detailInfo tourinfo $tourInfo")
            tourInfo?.let {
                AsyncImage(
                    model = it.image,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = it.title,
                    style = AppTypography.fontSize24ExtraBold,
                    modifier = Modifier
                        .padding(top = 20.dp, start = 16.dp, end = 16.dp)
                )

                Text(
                    text = it.address,
                    style = AppTypography.fontSize16SemiBold,
                    modifier = Modifier
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                )

                if (detailInfo.isNotEmpty()) {
                    when (it.contentTypeId) {
                        12 -> {
                            TourDetailContent("개장일:", detailInfo[0].openDate)
                            TourDetailContent("휴무일:", detailInfo[0].restDate)
                            TourDetailContent("이용 가능 시간:", detailInfo[0].useTime)
                        }

                        14 -> {
                            TourDetailContent("휴무일:", detailInfo[0].cultureRestDate)
                            TourDetailContent("이용 가능 시간:", detailInfo[0].cultureUseTime)
                            TourDetailContent("이용 요금:", detailInfo[0].culturePrice)
                            TourDetailContent("주차:", detailInfo[0].cultureParking)
                            TourDetailContent("주차 요금:", detailInfo[0].cultureParkingFee)
                            TourDetailContent("문의:", detailInfo[0].cultureInfoCenter)
                        }

                        15 -> {
                            TourDetailContent("행사 시작일:", detailInfo[0].eventStartDate)
                            TourDetailContent("행사 종료일:", detailInfo[0].eventEndDate)
                            TourDetailContent("행사 시간:", detailInfo[0].eventPlayTime)
                            TourDetailContent("장소:", detailInfo[0].eventPlace)
                            TourDetailContent("이용 금액:", detailInfo[0].eventUsePrice)
                            TourDetailContent("주최자:", detailInfo[0].eventSponsor)
                            TourDetailContent("주최자 문의:", detailInfo[0].eventSponsorTel)
                        }

                        28 -> {
                            TourDetailContent("휴무일:", detailInfo[0].activityRestDate)
                            TourDetailContent("이용 가능 시간:", detailInfo[0].activityUseTime)
                            TourDetailContent("이용 가능 연령:", detailInfo[0].activityPossibleAge)
                            TourDetailContent("주차 및 요금:", detailInfo[0].activityParking)
                            TourDetailContent("예약 안내:", detailInfo[0].activityReservation)
                            TourDetailContent("문의:", detailInfo[0].activityInfoCenter)
                        }

                        39 -> {
                            TourDetailContent("휴무일:", detailInfo[0].foodRestTime)
                            TourDetailContent("영업 시간:", detailInfo[0].foodOpenTime)
                            TourDetailContent("대표 메뉴:", detailInfo[0].foodFirstMenu)
                            TourDetailContent("메뉴:", detailInfo[0].foodTreatMenu)
                            TourDetailContent("포장:", detailInfo[0].foodTakeOut)
                            TourDetailContent("문의:", detailInfo[0].foodInfoCenter)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                .height(48.dp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(12.dp))
                .background(AppColors.ColorFF8C00)
                .clickable {
                    if(isLocationPermissionGranted) {
                        try {
                            tourInfo?.let { tourInfo ->
                                fusedLocationClient.lastLocation
                                    .addOnSuccessListener { location ->
                                        location?.let {
                                            val results = FloatArray(1)
                                            Location.distanceBetween(
                                                it.latitude,
                                                it.longitude,
                                                tourInfo.latitude,
                                                tourInfo.longitude,
                                                results
                                            )

                                            val distanceInMeters = results[0]
                                            if (distanceInMeters <= 300) {
                                                locationTourListViewModel.updateVisitStatus(tourInfo.contentId) { success, message ->
                                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                                                    if(success) {
                                                        navController.popBackStack()
                                                    }
                                                }
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "해당 장소와의 거리가 너무 멀어요! (${String.format("%.1f", distanceInMeters)}m)",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        } ?: run {
                                            Toast.makeText(
                                                context,
                                                "위치 정보를 가져올 수 없어요.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
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
                }
        ) {
            Text(
                text = "스탬프 찍기",
                style = AppTypography.fontSize20SemiBold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}