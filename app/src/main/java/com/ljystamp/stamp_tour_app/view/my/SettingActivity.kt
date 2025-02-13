package com.ljystamp.stamp_tour_app.view.my

import android.content.Intent
import android.os.Bundle
import com.ljystamp.stamp_tour_app.databinding.ActivitySettingBinding
import com.ljystamp.stamp_tour_app.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity: BaseActivity<ActivitySettingBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.run {
            tvAgreeContent.setOnSingleClickListener {
                val intent = Intent(this@SettingActivity, PrivacyActivity::class.java)
                startActivity(intent)
            }
            tvWithdraw.setOnSingleClickListener {
                val intent = Intent(this@SettingActivity, WithdrawActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun getViewBinding(): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }
}