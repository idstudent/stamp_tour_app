package com.ljystamp.stamp_tour_app.feature_home.presentation.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.ljystamp.common.presentation.viewmodel.LocationTourListViewModel
import com.ljystamp.core_ui.BaseFragment
import com.ljystamp.feature_home.databinding.FragmentHomeBinding
import com.ljystamp.stamp_tour_app.feature_home.presentation.adapter.InProgressStampAdapter
import com.ljystamp.utils.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.fragment.app.viewModels
import com.ljystamp.common.presentation.adapter.NearTourListAdapter

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private val locationTourListViewModel: LocationTourListViewModel by viewModels()
    private val auth = FirebaseAuth.getInstance()

    private val nearTourListAdapter by lazy {
        NearTourListAdapter(
            locationTourListViewModel,
            ::handleLoginRequest
        )
    }
    private lateinit var inProgressStampAdapter: InProgressStampAdapter
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

        initListener()
        observeNearTourList()
    }

    override fun onResume() {
        super.onResume()
        if (isLocationPermissionGranted) {
            getCurrentLocation()
        }

        val userId = auth.currentUser?.uid
        userId?.let {
            locationTourListViewModel.startObservingSavedLocations()
            nearTourListAdapter.notifyDataSetChanged()
        }
    }

    private fun setupAdapters() {
        inProgressStampAdapter = InProgressStampAdapter { savedLocation ->
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
                                if (distanceInMeters <= 300) {
                                    locationTourListViewModel.updateVisitStatus(savedLocation.contentId) { success, message ->
                                        message?.let { msg ->
                                            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "해당 장소와의 거리가 너무 멀어요! (${String.format("%.1f", distanceInMeters)}m)",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } ?: run {
                                Toast.makeText(
                                    requireContext(),
                                    "위치 정보를 가져올 수 없어요.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } catch (e: SecurityException) {
                    Toast.makeText(
                        requireContext(),
                        "위치 권한이 없습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else {
                Toast.makeText(requireContext(), "위치 권한이 필요해요.", Toast.LENGTH_SHORT).show()
            }
        }


        binding.run {
            rvNearTourList.layoutManager = LinearLayoutManager(activity)
            rvNearTourList.adapter = nearTourListAdapter

            rvStamp.apply {
                adapter = inProgressStampAdapter
                orientation = ViewPager2.ORIENTATION_HORIZONTAL
                offscreenPageLimit = 1
            }
        }
    }

    private fun observeSavedLocations() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                locationTourListViewModel.savedLocations.collect { locations ->
                    binding.run {
                        val notVisitedLocations = locations.filter { !it.isVisited }
                        inProgressStampAdapter.submitList(notVisitedLocations.take(5))

                        if (notVisitedLocations.isNotEmpty()) {
                            tvMyPlaceMore.isVisible = locations.size >= 5

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
                        getCurrentLocation()
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

    private fun getCurrentLocation() {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        val latitude = it.latitude
                        val longitude = it.longitude

                        locationTourListViewModel.getLocationTourList(longitude, latitude, 1, 12)
                    } ?: run {
                        Toast.makeText(
                            requireContext(),
                            "위치 정보를 가져올 수 없어요",
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
                "위치 권한이 없어요",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun handleLoginRequest() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
    }

    private fun initListener() {
        binding.run {
            tvNearPlaceMore.setOnSingleClickListener {
                val intent = Intent(requireActivity(), NearPlaceListActivity::class.java)
                intent.putExtra("typeId", 12)
                startActivity(intent)
            }

            tvMyPlaceMore.setOnSingleClickListener {
                val intent = Intent(requireActivity(), MyTourListActivity::class.java)
                startActivity(intent)
            }

            clCulture.setOnSingleClickListener {
                val intent = Intent(requireActivity(), NearPlaceListActivity::class.java)
                intent.putExtra("typeId", 14)
                startActivity(intent)
            }

            clFestival.setOnSingleClickListener {
                val intent = Intent(requireActivity(), NearPlaceListActivity::class.java)
                intent.putExtra("typeId", 15)
                startActivity(intent)
            }

            clActivity.setOnSingleClickListener {
                val intent = Intent(requireActivity(), NearPlaceListActivity::class.java)
                intent.putExtra("typeId", 28)
                startActivity(intent)
            }

            clFood.setOnSingleClickListener {
                val intent = Intent(requireActivity(), NearPlaceListActivity::class.java)
                intent.putExtra("typeId", 39)
                startActivity(intent)
            }
        }
    }

    private fun observeNearTourList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                locationTourListViewModel.nearTourList.collect {
                    binding.run {
                        if(it.isNotEmpty()) {
                            rvNearTourList.visibility = View.VISIBLE
                            clNullNearPlace.visibility = View.GONE
                            if(it.size > 4) {
                                tvNearPlaceMore.visibility = View.VISIBLE
                            } else {
                                tvNearPlaceMore.visibility = View.GONE
                            }
                            nearTourListAdapter.submitList(it.take(4))
                        } else {
                            rvNearTourList.visibility = View.GONE
                            clNullNearPlace.visibility = View.VISIBLE
                            tvNearPlaceMore.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }
}
