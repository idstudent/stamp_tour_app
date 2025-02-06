package com.ljystamp.stamp_tour_app.view.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import com.ljystamp.stamp_tour_app.view.home.NearPlaceListActivity
import com.ljystamp.stamp_tour_app.view.search.SearchListActivity
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
    private lateinit var recentlyListAdapter: SearchListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchListAdapter = SearchListAdapter(locationTourListViewModel, ::handleLoginRequest)
        recentlyListAdapter = SearchListAdapter(locationTourListViewModel, ::handleLoginRequest)

        binding.run {
            rvRecent.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            rvRecent.adapter = recentlyListAdapter

            rvNear.layoutManager = LinearLayoutManager(requireActivity())
            rvNear.adapter = searchListAdapter
        }

        if (savedInstanceState == null) {
            checkLocationPermission()
        }

        initListener()
        observeNearTourList()
    }

    override fun onResume() {
        super.onResume()

        binding.etSearch.setText("")

        viewLifecycleOwner.lifecycleScope.launch {
            locationTourListViewModel.selectRecentlySearchItem().collect {
                binding.run {
                    if (it.isNotEmpty()) {
                        clRecentNotResult.visibility = View.INVISIBLE
                        rvRecent.visibility = View.VISIBLE
                        recentlyListAdapter.submitList(it)
                        rvRecent.scrollToPosition(0)
                    }else {
                        clRecentNotResult.visibility = View.VISIBLE
                        rvRecent.visibility = View.INVISIBLE
                    }
                }
            }
        }
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

            tvNearPlaceMore.setOnSingleClickListener {
                val intent = Intent(requireActivity(), NearPlaceListActivity::class.java)
                intent.putExtra("typeId", contentTypeId)
                startActivity(intent)
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
                    // FINE_LOCATION 권한이 있는지 한번 더 체크
                    if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED) {
                        // 정확한 위치 권한이 있을 때만 허용 처리
                        isLocationPermissionGranted = true
                    } else {
                        // 대략적인 위치만 허용한 경우
                        isLocationPermissionGranted = false
                        Toast.makeText(
                            requireContext(),
                            "정확한 위치 확인을 위해 '정확한 위치' 권한을 허용해주세요",
                            Toast.LENGTH_LONG
                        ).show()
                    }
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
            .setDeniedMessage("정확한 위치 권한을 받지 않으면 몇몇 기능을 사용하지 못해요!\n정확한 위치를 켜시려면 설정 > 권한 > 위치 > 정확한 위치사용을 켜주세요")
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

                        locationTourListViewModel.getLocationTourList(longitude, latitude, 1, contentTypeId)
                    } ?: run {
                        Toast.makeText(
                            requireContext(),
                            "위치 정보를 가져올 수 없어요.",
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
                "위치 권한이 없어요.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun handleLoginRequest() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        activityResultLauncher.launch(intent)
    }

    private fun observeNearTourList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                locationTourListViewModel.nearTourList.collect {
                    binding.run {
                        if(it.isNotEmpty()) {
                            rvNear.visibility = View.VISIBLE
                            clNotSearch.visibility = View.GONE
                            if(it.size > 4) {
                                tvNearPlaceMore.visibility = View.VISIBLE
                            } else {
                                tvNearPlaceMore.visibility = View.GONE
                            }
                            searchListAdapter.submitList(it.take(4))
                        } else {
                            rvNear.visibility = View.GONE
                            clNotSearch.visibility = View.VISIBLE
                            tvNearPlaceMore.visibility = View.GONE
                        }
                    }
                }
            }
        }
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