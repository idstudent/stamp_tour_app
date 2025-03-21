package com.ljystamp.feature_search.presentation.view

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.ljystamp.common.presentation.viewmodel.LocationTourListViewModel
import com.ljystamp.core_ui.theme.AppColors
import com.ljystamp.core_ui.theme.AppTypography
import com.ljystamp.feature_search.R
import com.ljystamp.feature_search.presentation.component.SearchTourList
import com.ljystamp.feature_search.presentation.viewmodel.RecentlySearchViewModel

@Composable
fun SearchScreen(
    navController: NavController
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    var inputText by remember { mutableStateOf("") }

    val locationTourListViewModel: LocationTourListViewModel = hiltViewModel()
    val recentlySearchViewModel: RecentlySearchViewModel = hiltViewModel()

    var isLocationPermissionGranted by remember { mutableStateOf(false) }

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val searchTourList = recentlySearchViewModel.recentlySearchResult.collectAsState()
    val nearTourList = locationTourListViewModel.nearTourList.collectAsState()

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

    LaunchedEffect(Unit) {
        checkLocationPermission()
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if(event == Lifecycle.Event.ON_RESUME) {
                recentlySearchViewModel.selectRecentlySearchItem()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "검색",
            style = AppTypography.fontSize20ExtraBold,
            modifier = Modifier
                .padding(top = 32.dp, start = 20.dp)
        )

        Row(
           modifier = Modifier
               .padding(top = 48.dp, start = 20.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_tune_24),
                contentDescription = null,
                tint = AppColors.White,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "검색할 분류를 선택해주세요",
                style = AppTypography.fontSize16Regular,
                modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            BasicTextField(
                value = inputText,
                onValueChange = { inputText = it },
                textStyle = AppTypography.fontSize14Regular,
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .background(
                        color = AppColors.Color2A2A2A,
                        shape = RoundedCornerShape(24.dp)
                    ),
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        if (inputText.isEmpty()) {
                            Text(
                                text = "검색어를 입력해주세요",
                                style = AppTypography.fontSize14Regular,
                                color = AppColors.White
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .wrapContentSize()
                    .background(AppColors.ColorFF8C00, shape = RoundedCornerShape(24.dp))
                    .clickable { }
                    .padding(6.dp),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    painter = painterResource(id = R.drawable.baseline_search_24),
                    contentDescription = null,
                    tint = AppColors.White
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        )  {
            Text(
                text = "최근 검색",
                style = AppTypography.fontSize20ExtraBold,
            )

            if(searchTourList.value.isNotEmpty()) {
                Text(
                    text = "전체 삭제",
                    style = AppTypography.fontSize14Regular,
                    modifier = Modifier.clickable {

                    }
                )
            }
        }

        SearchTourList(
            navController = navController,
            tourList = searchTourList.value,
            locationTourListViewModel = locationTourListViewModel,
            emptyString = "최근 검색한 결과가 없어요"
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "내 근처에는 뭐가 있지?",
                style = AppTypography.fontSize20ExtraBold,
            )
            if(nearTourList.value.isNotEmpty()) {
                Text(
                    text = "더보기",
                    style = AppTypography.fontSize14Regular,
                    modifier = Modifier.clickable {

                    }
                )
            }
        }

        SearchTourList(
            navController = navController,
            tourList = nearTourList.value,
            locationTourListViewModel = locationTourListViewModel,
            emptyString = "필터를 선택하면 내 근처에\n뭐가 있는지 보여줘요!"
        )
    }
}