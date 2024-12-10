package com.ljystamp.stamp_tour_app.view.home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ljystamp.stamp_tour_app.api.model.TourMapper
import com.ljystamp.stamp_tour_app.databinding.ActivityNearPlaceListBinding
import com.ljystamp.stamp_tour_app.view.BaseActivity
import com.ljystamp.stamp_tour_app.view.adapter.NearTourListAdapter
import com.ljystamp.stamp_tour_app.viewmodel.LocationTourListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NearPlaceListActivity: BaseActivity<ActivityNearPlaceListBinding>() {
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val locationTourListViewModel: LocationTourListViewModel by viewModels()
    private lateinit var nearTourListAdapter: NearTourListAdapter
    private var page = 1
    private val currentTourList = mutableListOf<TourMapper>()
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nearTourListAdapter = NearTourListAdapter(locationTourListViewModel)

        search()

        binding.run {
            rvNearPlace.layoutManager = GridLayoutManager(this@NearPlaceListActivity, 2)
            rvNearPlace.adapter = nearTourListAdapter

            rvNearPlace.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val lastPos = layoutManager.findLastCompletelyVisibleItemPosition()

                    val totalCount = recyclerView.adapter?.itemCount
                    totalCount?.let { total ->
                        if (lastPos == total - 1) {
                            page++
                            search()
                        }
                    }
                }
            })
        }
    }

    private fun search() {
        if (isLoading) return

        isLoading = true
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        val latitude = it.latitude
                        val longitude = it.longitude

                        lifecycleScope.launch {
                            try {
                                locationTourListViewModel.getLocationTourList(
                                    longitude,
                                    latitude,
                                    page,
                                    12
                                ).collect { newTourList ->
                                    if (page == 1) {
                                        currentTourList.clear()
                                    }
                                    if (newTourList.isEmpty()) {
                                        // 더 이상 데이터가 없음
                                        return@collect
                                    }
                                    currentTourList.addAll(newTourList)
                                    nearTourListAdapter.submitList(currentTourList.toList())
                                }
                            } finally {
                                isLoading = false
                            }
                        }
                    } ?: run {
                        isLoading = false
                        Toast.makeText(this, "위치 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    isLoading = false
                    Toast.makeText(this, "위치 정보 조회 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } catch (e: SecurityException) {
            isLoading = false
            Toast.makeText(this, "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getViewBinding(): ActivityNearPlaceListBinding {
        return ActivityNearPlaceListBinding.inflate(layoutInflater)
    }
}