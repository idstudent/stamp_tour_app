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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.ljystamp.stamp_tour_app.FilterClickListener
import com.ljystamp.stamp_tour_app.databinding.FragmentSearchBinding
import com.ljystamp.stamp_tour_app.util.setOnSingleClickListener
import com.ljystamp.stamp_tour_app.view.BaseFragment
import com.ljystamp.stamp_tour_app.view.adapter.SearchListAdapter
import com.ljystamp.stamp_tour_app.view.search.SearchListActivity
import com.ljystamp.stamp_tour_app.view.user.LoginActivity
import com.ljystamp.stamp_tour_app.viewmodel.LocationTourListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment: BaseFragment<FragmentSearchBinding>() {
    private var contentTypeId = -1
    private var isLocationPermissionGranted = false
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private val locationTourListViewModel: LocationTourListViewModel by viewModels()

    private lateinit var searchListAdapter: SearchListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            checkLocationPermission()
            searchListAdapter = SearchListAdapter(locationTourListViewModel, ::handleLoginRequest)
            binding.rvNear.layoutManager = LinearLayoutManager(requireActivity())
            binding.rvNear.adapter = searchListAdapter
        }

        initListener()
    }

    override fun onResume() {
        super.onResume()

        binding.etSearch.setText("")
    }
    private fun initListener() {
        binding.run {
            ivFilter.setOnSingleClickListener {
                showFilterBottomSheet()
            }

            tvFilter.setOnSingleClickListener {
                showFilterBottomSheet()
            }

            ivSearch.setOnSingleClickListener {
                if(contentTypeId == -1) {
                    Toast.makeText(requireActivity(), "검색 조건을 선택하세요", Toast.LENGTH_SHORT).show()
                    return@setOnSingleClickListener
                }

                val inputText = etSearch.text.toString()

                if(inputText == "") {
                    Toast.makeText(requireActivity(), "검색어를 입력하세요", Toast.LENGTH_SHORT).show()
                    return@setOnSingleClickListener
                }else {
                    val intent = Intent(requireActivity(), SearchListActivity::class.java)
                    intent.putExtra("contentTypeId", contentTypeId)
                    intent.putExtra("keyword", inputText)
                    startActivity(intent)
                }


            }
        }
    }

    private fun showFilterBottomSheet() {
        val bottomSheet = SearchFilterBottomFragment()
        bottomSheet.setFilterClickListener(object: FilterClickListener {
            override fun onFilterSelected(filterType: Int) {
                contentTypeId = filterType

                binding.run {
                    when(contentTypeId) {
                        12 -> {
                            tvFilter.text = "여행지"
                        }
                        14 -> {
                            tvFilter.text = "문화"
                        }
                        15 -> {
                            tvFilter.text = "축제"
                        }
                        28 -> {
                            tvFilter.text = "액티비티"
                        }
                        39 -> {
                            tvFilter.text = "음식"
                        }
                    }
                }
                if(isLocationPermissionGranted) {
                    getCurrentLocation(contentTypeId)
                }
            }
        })
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }

    private fun checkLocationPermission() {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    isLocationPermissionGranted = true
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

    private fun getCurrentLocation(contentTypeId: Int) {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        val latitude = it.latitude
                        val longitude = it.longitude

                        Log.e("ljy", "위도: $latitude, 경도: $longitude")
                        viewLifecycleOwner.lifecycleScope.launch {

                            locationTourListViewModel.getLocationTourList(
                                longitude,
                                latitude,
                                1,
                                contentTypeId
                            ).collect { tourList ->
                                binding.run {
                                    if(tourList.isNotEmpty()) {
                                        rvNear.visibility = View.VISIBLE
                                        clRecentNotResult.visibility = View.GONE
                                        if(tourList.size > 4) {
                                            tvNearPlaceMore.visibility = View.VISIBLE
                                        } else {
                                            tvNearPlaceMore.visibility = View.GONE
                                        }
                                        searchListAdapter.submitList(tourList.take(4))
                                    } else {
                                        rvNear.visibility = View.GONE
                                        clRecentNotResult.visibility = View.VISIBLE
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
            searchListAdapter.notifyDataSetChanged()
        }
    }

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(layoutInflater)
    }
}