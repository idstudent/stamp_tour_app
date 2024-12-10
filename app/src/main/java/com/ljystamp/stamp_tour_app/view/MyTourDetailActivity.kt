package com.ljystamp.stamp_tour_app.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ljystamp.stamp_tour_app.R
import com.ljystamp.stamp_tour_app.databinding.ActivityMyTourDetailBinding

class MyTourDetailActivity: BaseActivity<ActivityMyTourDetailBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getViewBinding(): ActivityMyTourDetailBinding {
        return ActivityMyTourDetailBinding.inflate(layoutInflater)
    }
}