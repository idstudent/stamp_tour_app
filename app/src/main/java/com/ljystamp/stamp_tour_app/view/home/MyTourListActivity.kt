package com.ljystamp.stamp_tour_app.view.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
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

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkLocationPermission()

        myTourListAdapter = MyTourListAdapter(locationTourListViewModel) { savedLocation ->
            if(isLocationPermissionGranted) {
                try {
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location ->
                            location?.let {
                                val results = FloatArray(1)
                                Location.distanceBetween(
                                    it.latitude,
                                    it.longitude,
                                    savedLocation.latitude,
                                    savedLocation.longitude,
                                    results
                                )

                                val distanceInMeters = results[0]
                                if (distanceInMeters <= 500) {
                                    locationTourListViewModel.updateVisitStatus(savedLocation.contentId) { _, message ->
                                        message?.let { msg ->
                                            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(
                                        this,
                                        "해당 장소와의 거리가 너무 멀어요! (${String.format("%.1f", distanceInMeters)}m)",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } ?: run {
                                Toast.makeText(
                                    this,
                                    "위치 정보를 가져올 수 없어요.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } catch (e: SecurityException) {
                    Toast.makeText(
                        this,
                        "위치 권한이 없어요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else {
                Toast.makeText(this, "위치 권한이 필요해요.", Toast.LENGTH_SHORT).show()
            }
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
    private fun checkLocationPermission() {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    // FINE_LOCATION 권한이 있는지 한번 더 체크
                    if (ActivityCompat.checkSelfPermission(
                            this@MyTourListActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED) {
                        // 정확한 위치 권한이 있을 때만 허용 처리
                        isLocationPermissionGranted = true
                    } else {
                        // 대략적인 위치만 허용한 경우
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