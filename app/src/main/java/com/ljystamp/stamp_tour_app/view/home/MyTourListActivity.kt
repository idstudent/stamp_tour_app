package com.ljystamp.stamp_tour_app.view.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.GnssStatus
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.location.*
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.ljystamp.stamp_tour_app.api.model.SavedLocation
import com.ljystamp.stamp_tour_app.databinding.ActivityMyTourListBinding
import com.ljystamp.stamp_tour_app.view.BaseActivity
import com.ljystamp.stamp_tour_app.view.adapter.MyTourListAdapter
import com.ljystamp.stamp_tour_app.viewmodel.LocationTourListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyTourListActivity: BaseActivity<ActivityMyTourListBinding>() {
    private val locationTourListViewModel: LocationTourListViewModel by viewModels()
    private var myTourListAdapter: MyTourListAdapter? = null
    private var isLocationPermissionGranted = false
    private var isOutdoor = false

    private val locationManager: LocationManager by lazy {
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val gnssCallback = object : GnssStatus.Callback() {
        override fun onSatelliteStatusChanged(status: GnssStatus) {
            var strongSignals = 0
            for (i in 0 until status.satelliteCount) {
                if (status.getCn0DbHz(i) > 20.0f) {
                    strongSignals++
                }
            }

            isOutdoor = strongSignals >= 4
            Log.d("GNSS", "강한 신호의 위성 수: $strongSignals, 실외 여부: $isOutdoor")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.registerGnssStatusCallback(gnssCallback, null)
        }

        checkLocationPermission()

        myTourListAdapter = MyTourListAdapter(locationTourListViewModel) { savedLocation ->
            if (!isLocationPermissionGranted) {
                Toast.makeText(this, "위치 권한이 필요해요.", Toast.LENGTH_SHORT).show()
                return@MyTourListAdapter
            }

            if (!isOutdoor) {
                Toast.makeText(
                    this,
                    "실내에서는 스탬프를 찍을 수 없어요. 실외로 이동해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
                return@MyTourListAdapter
            }

            getCurrentLocation(savedLocation)
        }

        binding.run {
            rvMyTour.layoutManager = GridLayoutManager(this@MyTourListActivity, 2)
            rvMyTour.adapter = myTourListAdapter

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    locationTourListViewModel.savedLocations.collect { locations ->
                        val notVisitedLocations = locations.filter { !it.isVisited }
                        myTourListAdapter?.submitList(notVisitedLocations)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.unregisterGnssStatusCallback(gnssCallback)
    }

    private fun getCurrentLocation(savedLocation: SavedLocation) {
        try {
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setWaitForAccurateLocation(true)
                .setMinUpdateIntervalMillis(5000)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { location ->
                        // 위치 정확도가 50m 이내인 경우에만 처리
                        if (location.accuracy <= 50) {
                            val results = FloatArray(1)
                            Location.distanceBetween(
                                location.latitude,
                                location.longitude,
                                savedLocation.latitude,
                                savedLocation.longitude,
                                results
                            )
                            val distanceInMeters = results[0]
                            Log.d("Location", "현재 위치 - 위도: ${location.latitude}, 경도: ${location.longitude}, 정확도: ${location.accuracy}m")
                            Log.d("Location", "목표 위치 - 위도: ${savedLocation.latitude}, 경도: ${savedLocation.longitude}, 거리: ${distanceInMeters}m")

                            if (distanceInMeters <= 500) {
                                locationTourListViewModel.updateVisitStatus(savedLocation.contentId) { _, message ->
                                    message?.let { msg ->
                                        Toast.makeText(this@MyTourListActivity, msg, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    this@MyTourListActivity,
                                    "해당 장소와의 거리가 너무 멀어요! (${String.format("%.1f", distanceInMeters)}m)",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            // 위치 확인 후 업데이트 중단
                            fusedLocationClient.removeLocationUpdates(this)
                        } else {
                            Toast.makeText(
                                this@MyTourListActivity,
                                "위치 정확도가 낮아요. GPS 신호가 더 좋은 곳으로 이동해주세요.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        } catch (e: SecurityException) {
            Toast.makeText(this, "위치 권한이 없어요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkLocationPermission() {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    if (ActivityCompat.checkSelfPermission(
                            this@MyTourListActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED) {
                        isLocationPermissionGranted = true
                        // 권한이 승인되면 GNSS 콜백 등록
                        locationManager.registerGnssStatusCallback(gnssCallback, null)
                    } else {
                        isLocationPermissionGranted = false
                        Toast.makeText(
                            this@MyTourListActivity,
                            "정확한 위치 확인을 위해 '정확한 위치' 권한을 허용해주세요",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onPermissionDenied(deniedPermissions: List<String>) {
                    isLocationPermissionGranted = false
                    Toast.makeText(
                        this@MyTourListActivity,
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

    override fun getViewBinding(): ActivityMyTourListBinding {
        return ActivityMyTourListBinding.inflate(layoutInflater)
    }
}