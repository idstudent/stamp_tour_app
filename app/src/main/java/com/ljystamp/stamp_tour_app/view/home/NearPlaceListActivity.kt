package com.ljystamp.stamp_tour_app.view.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ljystamp.stamp_tour_app.api.model.TourMapper
import com.ljystamp.stamp_tour_app.databinding.ActivityNearPlaceListBinding
import com.ljystamp.stamp_tour_app.view.BaseActivity
import com.ljystamp.stamp_tour_app.view.adapter.NearTourListAdapter
import com.ljystamp.stamp_tour_app.view.user.LoginActivity
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

    private var contentTypeId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nearTourListAdapter = NearTourListAdapter(locationTourListViewModel, ::handleLoginRequest)

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

        val intent = intent
        contentTypeId = intent.getIntExtra("typeId", -1)

        isLoading = true
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        val latitude = it.latitude
                        val longitude = it.longitude

                        lifecycleScope.launch {
                            repeatOnLifecycle(Lifecycle.State.STARTED) {
                                try {
                                    contentTypeId?.let { typeId ->
                                        binding.run {
                                            when(contentTypeId) {
                                                12 -> tvTitle.text = "내 근처 여행지"
                                                14 -> tvTitle.text = "내 근처 문화 시설"
                                                15 -> tvTitle.text = "내 근처 축제"
                                                28 -> tvTitle.text = "내 근처 액티비티"
                                                39 -> tvTitle.text = "내 근처 식당"
                                            }
                                        }

                                        locationTourListViewModel.getLocationTourList(
                                            longitude,
                                            latitude,
                                            page,
                                            typeId
                                        ).collect { newTourList ->
                                            if (page == 1) {
                                                currentTourList.clear()
                                            }
                                            if (newTourList.isEmpty()) {
                                                return@collect
                                            }
                                            currentTourList.addAll(newTourList)
                                            nearTourListAdapter.submitList(currentTourList.toList())
                                        }
                                    }
                                } finally {
                                    isLoading = false
                                }
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
    private fun handleLoginRequest() {
        val intent = Intent(this, LoginActivity::class.java)
        activityResultLauncher.launch(intent)
    }

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            locationTourListViewModel.startObservingSavedLocations()
            nearTourListAdapter.notifyDataSetChanged()
            search()
        }
    }
    override fun getViewBinding(): ActivityNearPlaceListBinding {
        return ActivityNearPlaceListBinding.inflate(layoutInflater)
    }
}