package com.ljystamp.feature_near_place.presentation.view

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
import com.ljystamp.common.presentation.adapter.NearTourListAdapter
import com.ljystamp.common.presentation.view.LoginActivity
import com.ljystamp.common.presentation.viewmodel.LocationTourListViewModel
import com.ljystamp.core_navigation.Navigator
import com.ljystamp.core_ui.BaseActivity
import com.ljystamp.feature_near_place.databinding.ActivityNearPlaceListBinding
import com.ljystamp.stamp_tour_app.model.TourMapper
import com.ljystamp.utils.setOnSingleClickListener
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

        setupRecyclerView()
        observeNearTourList()
        search()
        initListener()
    }

    private fun search() {
        if (isLoading) return
        isLoading = true

        contentTypeId = intent.getIntExtra("typeId", -1)
        updateTitle()

        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        contentTypeId?.let { typeId ->
                            locationTourListViewModel.getLocationTourList(
                                it.longitude,
                                it.latitude,
                                page,
                                typeId
                            )
                        }
                    } ?: run {
                        isLoading = false
                        Toast.makeText(this, "위치 정보를 가져올 수 없어요.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    isLoading = false
                    Toast.makeText(this, "위치 정보 조회 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } catch (e: SecurityException) {
            isLoading = false
            Toast.makeText(this, "위치 권한이 없어요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTitle() {
        binding.tvTitle.text = when(contentTypeId) {
            12 -> "내 근처 여행지"
            14 -> "내 근처 문화 시설"
            15 -> "내 근처 축제"
            28 -> "내 근처 액티비티"
            39 -> "내 근처 식당"
            else -> ""
        }
    }

    private fun observeNearTourList() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                locationTourListViewModel.nearTourList.collect { newTourList ->
                    if (page == 1) {
                        currentTourList.clear()
                    }
                    if (newTourList.isEmpty()) {
                        isLoading = false
                        return@collect
                    }
                    currentTourList.addAll(newTourList)
                    nearTourListAdapter.submitList(currentTourList.toList())
                    isLoading = false
                }
            }
        }
    }

    private fun setupRecyclerView() {
        nearTourListAdapter = NearTourListAdapter(locationTourListViewModel, ::handleLoginRequest)

        binding.rvNearPlace.apply {
            layoutManager = GridLayoutManager(this@NearPlaceListActivity, 2)
            adapter = nearTourListAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val lastPos = layoutManager.findLastCompletelyVisibleItemPosition()
                    val totalCount = recyclerView.adapter?.itemCount

                    if (!isLoading && totalCount != null && lastPos == totalCount - 1) {
                        page++
                        search()
                    }
                }
            })
        }
    }

    private fun initListener() {
        binding.ivMap.setOnSingleClickListener {
            Navigator.navigateKakaoMap(this@NearPlaceListActivity)
        }
    }
    private fun handleLoginRequest() {
        val intent = Intent(this, LoginActivity::class.java)
        activityResultLauncher.launch(intent)
    }

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            locationTourListViewModel.startObservingSavedLocations()
            nearTourListAdapter.notifyDataSetChanged()
            search()
        }
    }
    override fun getViewBinding(): ActivityNearPlaceListBinding {
        return ActivityNearPlaceListBinding.inflate(layoutInflater)
    }
}