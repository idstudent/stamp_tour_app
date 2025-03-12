package com.ljystamp.feature_home.presentation.view

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.ljystamp.common.presentation.viewmodel.LocationTourListViewModel
import com.ljystamp.core_ui.theme.AppTypography
import com.ljystamp.feature_home.presentation.coponent.HomeCategorySection
import com.ljystamp.feature_home.presentation.coponent.StampTourViewPager
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.ljystamp.feature_home.presentation.coponent.HomeNearTourList

@Composable
fun HomeScreen() {
    val locationTourListViewModel: LocationTourListViewModel = hiltViewModel()
    val savedLocations = locationTourListViewModel.savedLocations.collectAsState()
    val nearTourList = locationTourListViewModel.nearTourList.collectAsState()

    var isLocationPermissionGranted by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val notVisitedLocations = savedLocations.value.filter { !it.isVisited }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "진행 중인 스탬프",
                style = AppTypography.fontSize24ExtraBold,
            )

            if (notVisitedLocations.size > 5) {
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

        Spacer(modifier = Modifier.padding(top = 24.dp))

        HomeCategorySection(context = context)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "근처 여행지",
                style = AppTypography.fontSize24ExtraBold,
            )

            if (nearTourList.value.size > 4) {
                Text(
                    text = "더보기",
                    style = AppTypography.fontSize14Regular,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HomeNearTourList(nearTourList = nearTourList.value, locationTourListViewModel = locationTourListViewModel)
        Spacer(modifier = Modifier.height(40.dp))

    }

    fun getCurrentLocation() {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        val latitude = it.latitude
                        val longitude = it.longitude

                        locationTourListViewModel.getLocationTourList(longitude, latitude, 1, 12)
                    } ?: run {
                        Toast.makeText(
                            context,
                            "위치 정보를 가져올 수 없어요",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        context,
                        "위치 정보 조회 실패: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } catch (e: SecurityException) {
            Toast.makeText(
                context,
                "위치 권한이 없어요",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun checkLocationPermission() {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED) {
                        // 정확한 위치 권한이 있을 때만 허용 처리
                        isLocationPermissionGranted = true
                        getCurrentLocation()
                    } else {
                        // 대략적인 위치만 허용한 경우
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

    // 처음 실행시 권한 확인
    LaunchedEffect(Unit) {
        checkLocationPermission()
    }

    // onresume
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (isLocationPermissionGranted) {
                    getCurrentLocation()
                }

                val auth = FirebaseAuth.getInstance()
                val userId = auth.currentUser?.uid
                userId?.let {
                    locationTourListViewModel.startObservingSavedLocations()
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}