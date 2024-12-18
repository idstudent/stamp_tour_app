package com.ljystamp.stamp_tour_app

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ljystamp.stamp_tour_app.databinding.ActivityMainBinding
import com.ljystamp.stamp_tour_app.view.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: BaseActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navBar.setupWithNavController(navController)
    }

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}
//private lateinit var fusedLocationClient: FusedLocationProviderClient
//    // 스탬프 찍을 위치
//    private val targetLatitude = 37.2735894
//    private val targetLongitude = 127.0147916
//
//    private val locationTourListViewModel: LocationTourListViewModel by viewModels()
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//
//        val checkButton = findViewById<Button>(R.id.btnCheck)
//        checkButton.setOnClickListener {
//            if (checkLocationPermission()) {
//                checkDistance()
//            } else {
//                requestLocationPermission()
//            }
//        }
//    }
//
//    private fun checkDistance() {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
//                .addOnSuccessListener { location ->
//                    location?.let {
//                        val results = FloatArray(1)
//                        Location.distanceBetween(
//                            location.latitude,
//                            location.longitude,
//                            targetLatitude,
//                            targetLongitude,
//                            results
//                        )
//
//                        val distanceInMeters = results[0]
//
//                        // 상세 로그 추가
//                        Log.d("Location", "현재 위치 - 위도: ${location.latitude}, 경도: ${location.longitude}")
//                        Log.d("Location", "목표 위치 - 위도: $targetLatitude, 경도: $targetLongitude")
//                        Log.d("Location", "GPS 정확도: ${location.accuracy}m")
//                        Log.d("Location", "계산된 거리: ${distanceInMeters}m")
//
//                        if (distanceInMeters <= 10) {
//                            Toast.makeText(this@MainActivity,
//                                "성공 위도: ${location.latitude}, 경도: ${location.lon gitude} (${String.format("%.2f", distanceInMeters)}m)",
//                                Toast.LENGTH_SHORT).show()
//                            Log.d("Location", "위도: ${location.latitude}, 경도: ${location.longitude} 스탬프 찍기 성공 - 거리: ${distanceInMeters}m")
//                        } else {
//                            Toast.makeText(this@MainActivity,
//                                "실패 위도: ${location.latitude}, 경도: ${location.longitude} (${String.format("%.2f", distanceInMeters)}m)",
//                                Toast.LENGTH_SHORT).show()
//                            Log.d("Location", "스탬프 찍기 실패 - 거리: ${distanceInMeters}m")
//                        }
//                    } ?: run {
//                        Toast.makeText(this, "위치를 가져올 수 없습니다", Toast.LENGTH_SHORT).show()
//                    }
//                }
//        }
//    }
//
//    private fun checkLocationPermission(): Boolean {
//        return ActivityCompat.checkSelfPermission(
//            this,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun requestLocationPermission() {
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//            LOCATION_PERMISSION_REQUEST_CODE
//        )
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            LOCATION_PERMISSION_REQUEST_CODE -> {
//                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    checkDistance()
//                } else {
//                    Toast.makeText(this, "위치 권한이 필요합니다", Toast.LENGTH_SHORT).show()
//                }
//                return
//            }
//        }
//    }
//
//    companion object {
//        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
//    }
//}