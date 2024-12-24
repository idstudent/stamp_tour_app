package com.ljystamp.stamp_tour_app.view.my

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ljystamp.stamp_tour_app.R
import com.ljystamp.stamp_tour_app.databinding.ActivitySettingBinding
import com.ljystamp.stamp_tour_app.util.setOnSingleClickListener
import com.ljystamp.stamp_tour_app.view.BaseActivity
import com.ljystamp.stamp_tour_app.viewmodel.UserViewModel
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