package com.ljystamp.stamp_tour_app.view.search

import android.os.Bundle
import com.ljystamp.stamp_tour_app.databinding.ActivitySearchTourDetailBinding
import com.ljystamp.stamp_tour_app.view.BaseActivity

class SearchTourDetailActivity: BaseActivity<ActivitySearchTourDetailBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getViewBinding(): ActivitySearchTourDetailBinding {
        return ActivitySearchTourDetailBinding.inflate(layoutInflater)
    }
}