package com.ljystamp.stamp_tour_app.view.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.GnssStatus
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.ljystamp.stamp_tour_app.databinding.FragmentHomeBinding
import com.ljystamp.stamp_tour_app.util.setOnSingleClickListener
import com.ljystamp.stamp_tour_app.view.BaseFragment
import com.ljystamp.stamp_tour_app.view.user.LoginActivity
import com.ljystamp.stamp_tour_app.view.home.NearPlaceListActivity
import com.ljystamp.stamp_tour_app.view.adapter.NearTourListAdapter
import com.ljystamp.stamp_tour_app.view.adapter.SavedLocationsAdapter
import com.ljystamp.stamp_tour_app.view.home.MyTourListActivity
import com.ljystamp.stamp_tour_app.viewmodel.LocationTourListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import android.util.Log

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private val locationManager: LocationManager by lazy {
        requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private var isOutdoor = false
    private val locationTourListViewModel: LocationTourListViewModel by viewModels()
    private val auth = FirebaseAuth.getInstance()

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

    private val nearTourListAdapter by lazy {
        NearTourListAdapter(
            locationTourListViewModel,
            ::handleLoginRequest
        )
    }
    private lateinit var savedLocationsAdapter: SavedLocationsAdapter
    private var isLocationPermissionGranted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.registerGnssStatusCallback(gnssCallback, null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.unregisterGnssStatusCallback(gnssCallback)
    }

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
        } ?: run {
            // userId가 null일 경우 (로그아웃 상태)
            locationTourListViewModel.clearSavedLocations()  // ViewModel에 clear 메서드 추가 필요
            savedLocationsAdapter.submitList(emptyList())
        }
    }


    private fun setupAdapters() {
        savedLocationsAdapter = SavedLocationsAdapter(locationTourListViewModel) { savedLocation ->
            if (!isLocationPermissionGranted) {
                Toast.makeText(requireContext(), "위치 권한이 필요해요.", Toast.LENGTH_SHORT).show()
                return@SavedLocationsAdapter
            }

            if (!isOutdoor) {
                Toast.makeText(
                    requireContext(),
                    "실내에서는 스탬프를 찍을 수 없어요. 실외로 이동해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
                return@SavedLocationsAdapter
            }

            try {
                val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                    .setWaitForAccurateLocation(true)
                    .setMinUpdateIntervalMillis(5000)
                    .build()

                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        locationResult.lastLocation?.let { location ->
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
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "위치 정확도가 낮아요. GPS 신호가 더 좋은 곳으로 이동해주세요.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            fusedLocationClient.removeLocationUpdates(this)
                        }
                    }
                }

                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
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
                Toast.makeText(requireContext(), "위치 권한이 없어요.", Toast.LENGTH_SHORT).show()
            }
        }

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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                locationTourListViewModel.savedLocations.collect { locations ->
                    binding.run {
                        val notVisitedLocations = locations.filter { !it.isVisited }
                        savedLocationsAdapter.submitList(notVisitedLocations.take(5))

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
                    if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED) {
                        isLocationPermissionGranted = true
                        getCurrentLocation()
                    } else {
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
        if (!isLocationPermissionGranted) {
            Toast.makeText(requireContext(), "위치 권한이 필요해요.", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setWaitForAccurateLocation(true)
                .setMinUpdateIntervalMillis(5000)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { location ->
                        locationTourListViewModel.getLocationTourList(location.longitude, location.latitude, 1, 12)
                        fusedLocationClient.removeLocationUpdates(this)
                    }
                }
            }

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
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
            Toast.makeText(requireContext(), "위치 권한이 없어요.", Toast.LENGTH_SHORT).show()
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

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }
}