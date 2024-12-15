package com.ljystamp.stamp_tour_app.view.my

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ljystamp.stamp_tour_app.R
import com.ljystamp.stamp_tour_app.databinding.ActivityMyCompleteListBinding
import com.ljystamp.stamp_tour_app.view.BaseActivity

class MyCompleteListActivity: BaseActivity<ActivityMyCompleteListBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getViewBinding(): ActivityMyCompleteListBinding {
        return ActivityMyCompleteListBinding.inflate(layoutInflater)
    }
}