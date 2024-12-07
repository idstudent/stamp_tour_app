package com.ljystamp.stamp_tour_app.view.main

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.ljystamp.stamp_tour_app.databinding.FragmentHomeBinding
import com.ljystamp.stamp_tour_app.view.BaseFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ljystamp.stamp_tour_app.view.adapter.NearTourListAdapter
import com.ljystamp.stamp_tour_app.viewmodel.LocationTourListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private val locationTourListViewModel: LocationTourListViewModel by viewModels()
    private lateinit var nearTourListAdapter: NearTourListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        if (savedInstanceState == null) {
            nearTourListAdapter = NearTourListAdapter(locationTourListViewModel)

            binding.run {
                rvNearTourList.layoutManager = LinearLayoutManager(activity)
                rvNearTourList.adapter = nearTourListAdapter
            }
            checkLocationPermission()
        }

        return view
    }


    private fun checkLocationPermission() {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    getCurrentLocation()
                }

                override fun onPermissionDenied(deniedPermissions: List<String>) {
                    Toast.makeText(
                        requireContext(),
                        "위치 권한이 거부되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
            .setDeniedMessage("위치 권한을 받지 않으면 몇몇 기능을 사용하지 못해요!")
            .setPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .check()
    }

    private fun getCurrentLocation() {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        val latitude = it.latitude   // 위도
                        val longitude = it.longitude // 경도

                        Log.e("ljy", "위도: $latitude, 경도: $longitude")

                        lifecycleScope.launch {
                            locationTourListViewModel.getLocationTourList(
                                longitude,
                                latitude,
                                12
                            )
                                .collect { tourList ->
                                    if(tourList.isNotEmpty()) {
                                        binding.rvNearTourList.visibility = View.VISIBLE
                                        binding.clNullNearPlace.visibility = View.GONE
                                        nearTourListAdapter.submitList(tourList.take(4))
                                    }else {
                                        binding.rvNearTourList.visibility = View.GONE
                                        binding.clNullNearPlace.visibility = View.VISIBLE
                                    }

                                }
                        }
                    }?: run {
                        Toast.makeText(
                            requireContext(),
                            "위치 정보를 가져올 수 없습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        requireContext(),
                        "위치 정보 조회 실패: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } catch (e: SecurityException) {
            Toast.makeText(
                requireContext(),
                "위치 권한이 없습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }
}