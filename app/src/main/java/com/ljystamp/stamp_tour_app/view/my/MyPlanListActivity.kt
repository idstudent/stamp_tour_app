package com.ljystamp.stamp_tour_app.view.my

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ljystamp.stamp_tour_app.R
import com.ljystamp.stamp_tour_app.databinding.ActivityMyPlanListBinding
import com.ljystamp.stamp_tour_app.view.BaseActivity

class MyPlanListActivity: BaseActivity<ActivityMyPlanListBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getViewBinding(): ActivityMyPlanListBinding {
        return ActivityMyPlanListBinding.inflate(layoutInflater)
    }
}