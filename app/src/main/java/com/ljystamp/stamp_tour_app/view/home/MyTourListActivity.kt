package com.ljystamp.stamp_tour_app.view.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myTourListAdapter = MyTourListAdapter(locationTourListViewModel)

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

    override fun getViewBinding(): ActivityMyTourListBinding {
        return ActivityMyTourListBinding.inflate(layoutInflater)
    }
}