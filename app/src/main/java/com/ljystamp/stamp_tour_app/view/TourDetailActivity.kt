package com.ljystamp.stamp_tour_app.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ljystamp.stamp_tour_app.R
import com.ljystamp.stamp_tour_app.databinding.ActivityTourDetailBinding

class TourDetailActivity: BaseActivity<ActivityTourDetailBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getViewBinding(): ActivityTourDetailBinding {
        return ActivityTourDetailBinding.inflate(layoutInflater)
    }
}