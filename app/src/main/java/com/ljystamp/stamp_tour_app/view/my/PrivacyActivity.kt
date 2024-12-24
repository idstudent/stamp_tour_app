package com.ljystamp.stamp_tour_app.view.my

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ljystamp.stamp_tour_app.R
import com.ljystamp.stamp_tour_app.databinding.ActivityPrivacyBinding
import com.ljystamp.stamp_tour_app.view.BaseActivity

class PrivacyActivity: BaseActivity<ActivityPrivacyBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun getViewBinding(): ActivityPrivacyBinding {
        return ActivityPrivacyBinding.inflate(layoutInflater)
    }
}