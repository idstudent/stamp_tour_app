package com.ljystamp.feature_my.presentation.view

import android.content.Intent
import android.os.Bundle
import com.ljystamp.core_ui.BaseActivity
import com.ljystamp.feature_auth.presentation.view.WithdrawActivity
import com.ljystamp.feature_my.databinding.ActivitySettingBinding
import com.ljystamp.utils.setOnSingleClickListener
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