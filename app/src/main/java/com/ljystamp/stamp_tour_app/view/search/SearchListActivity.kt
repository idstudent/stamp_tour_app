package com.ljystamp.stamp_tour_app.view.search

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ljystamp.stamp_tour_app.R
import com.ljystamp.stamp_tour_app.api.model.TourMapper
import com.ljystamp.stamp_tour_app.databinding.ActivitySearchListBinding
import com.ljystamp.stamp_tour_app.view.BaseActivity
import com.ljystamp.stamp_tour_app.view.adapter.SearchListAdapter
import com.ljystamp.stamp_tour_app.view.user.LoginActivity
import com.ljystamp.stamp_tour_app.viewmodel.LocationTourListViewModel
import com.ljystamp.stamp_tour_app.viewmodel.SearchKeywordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchListActivity: BaseActivity<ActivitySearchListBinding>() {
    private lateinit var searchListAdapter: SearchListAdapter
    private val locationTourListViewModel: LocationTourListViewModel by viewModels()
    private val searchKeywordViewModel: SearchKeywordViewModel by viewModels()

    private var page = 1
    private var isLoading = false
    private var contentTypeId = -1
    private var keyword = ""
    private val currentResultList = mutableListOf<TourMapper>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchListAdapter = SearchListAdapter(locationTourListViewModel, ::handleLoginRequest)

        val intent = intent
        contentTypeId = intent.getIntExtra("contentTypeId", -1)
        keyword = intent.getStringExtra("keyword") ?: ""

        if(contentTypeId != -1 && keyword != "") {
            search()
        }

        binding.run {
            rvSearch.layoutManager = GridLayoutManager(this@SearchListActivity, 2)
            rvSearch.adapter = searchListAdapter

            rvSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchKeywordViewModel.getSearchKeywordResult(keyword, contentTypeId).collect {
                    if(page == 1) {
                        currentResultList.clear()
                    }
                    if(it.isEmpty()) {
                        return@collect
                    }

                    currentResultList.addAll(it)
                    searchListAdapter.submitList(currentResultList)
                }
            }
        }
    }

    private fun handleLoginRequest() {
        val intent = Intent(this, LoginActivity::class.java)
        activityResultLauncher.launch(intent)
    }

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            locationTourListViewModel.startObservingSavedLocations()
            searchListAdapter.notifyDataSetChanged()
            search()
        }
    }
    override fun getViewBinding(): ActivitySearchListBinding {
        return ActivitySearchListBinding.inflate(layoutInflater)
    }
}