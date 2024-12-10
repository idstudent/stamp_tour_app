package com.ljystamp.stamp_tour_app.view.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.ljystamp.stamp_tour_app.databinding.FragmentHomeBinding
import com.ljystamp.stamp_tour_app.util.setOnSingleClickListener
import com.ljystamp.stamp_tour_app.view.BaseFragment
import com.ljystamp.stamp_tour_app.view.LoginActivity
import com.ljystamp.stamp_tour_app.view.home.NearPlaceListActivity
import com.ljystamp.stamp_tour_app.view.adapter.NearTourListAdapter
import com.ljystamp.stamp_tour_app.view.adapter.SavedLocationsAdapter
import com.ljystamp.stamp_tour_app.view.home.MyTourListActivity
import com.ljystamp.stamp_tour_app.viewmodel.LocationTourListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private val locationTourListViewModel: LocationTourListViewModel by viewModels()
    private var locationJob: Job? = null

    private val nearTourListAdapter by lazy {
        NearTourListAdapter(
            locationTourListViewModel,
            ::handleLoginRequest
        )
    }
    private lateinit var savedLocationsAdapter: SavedLocationsAdapter
    private var isLocationPermissionGranted = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        if (savedInstanceState == null) {
            setupAdapters()
            observeSavedLocations()
            checkLocationPermission()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            tvNearPlaceMore.setOnSingleClickListener {
                val intent = Intent(requireActivity(), NearPlaceListActivity::class.java)
                startActivity(intent)
            }

            tvMyPlaceMore.setOnSingleClickListener {
                val intent = Intent(requireActivity(), MyTourListActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isLocationPermissionGranted) {
            getCurrentLocation()
        }
    }

    override fun onDestroyView() {
        locationJob?.cancel()
        super.onDestroyView()
    }

    private fun setupAdapters() {
        savedLocationsAdapter = SavedLocationsAdapter()

        binding.run {
            rvNearTourList.layoutManager = LinearLayoutManager(activity)
            rvNearTourList.adapter = nearTourListAdapter

            rvStamp.apply {
                adapter = savedLocationsAdapter
                orientation = ViewPager2.ORIENTATION_HORIZONTAL
                offscreenPageLimit = 1
            }
        }
    }

    private fun observeSavedLocations() {
        viewLifecycleOwner.lifecycleScope.launch {
            locationTourListViewModel.savedLocations.collect { locations ->
                binding.run {
                    savedLocationsAdapter.submitList(locations.take(5))

                    if (locations.isNotEmpty()) {
                        rvStamp.visibility = View.VISIBLE
                        clNullTodayStamp.visibility = View.INVISIBLE
                        wormDotsIndicator.visibility = View.VISIBLE
                        wormDotsIndicator.attachTo(rvStamp)
                    } else {
                        rvStamp.visibility = View.INVISIBLE
                        clNullTodayStamp.visibility = View.VISIBLE
                        wormDotsIndicator.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun checkLocationPermission() {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    isLocationPermissionGranted = true
                    getCurrentLocation()
                }

                override fun onPermissionDenied(deniedPermissions: List<String>) {
                    isLocationPermissionGranted = false
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
        locationJob?.cancel()

        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        val latitude = it.latitude
                        val longitude = it.longitude

                        Log.e("ljy", "위도: $latitude, 경도: $longitude")

                        locationJob = viewLifecycleOwner.lifecycleScope.launch {
                            locationTourListViewModel.getLocationTourList(
                                longitude,
                                latitude,
                                1,
                                12
                            ).collect { tourList ->
                                binding.run {
                                    if(tourList.isNotEmpty()) {
                                        rvNearTourList.visibility = View.VISIBLE
                                        clNullNearPlace.visibility = View.GONE
                                        if(tourList.size > 4) {
                                            tvNearPlaceMore.visibility = View.VISIBLE
                                        } else {
                                            tvNearPlaceMore.visibility = View.GONE
                                        }
                                        nearTourListAdapter.submitList(tourList.take(4))
                                    } else {
                                        rvNearTourList.visibility = View.GONE
                                        clNullNearPlace.visibility = View.VISIBLE
                                        tvNearPlaceMore.visibility = View.GONE
                                    }
                                }
                            }
                        }
                    } ?: run {
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

    private fun handleLoginRequest() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        activityResultLauncher.launch(intent)
    }

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            locationTourListViewModel.startObservingSavedLocations()
            nearTourListAdapter.notifyDataSetChanged()
            getCurrentLocation()
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }
}