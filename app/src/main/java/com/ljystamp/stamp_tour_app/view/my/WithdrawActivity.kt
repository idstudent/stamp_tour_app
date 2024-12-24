package com.ljystamp.stamp_tour_app.view.my

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.ljystamp.stamp_tour_app.databinding.ActivityWithdrawBinding
import com.ljystamp.stamp_tour_app.util.setOnSingleClickListener
import com.ljystamp.stamp_tour_app.view.BaseActivity
import com.ljystamp.stamp_tour_app.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WithdrawActivity: BaseActivity<ActivityWithdrawBinding>() {
    private val userViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.run {
            tvWithdraw.setOnSingleClickListener {
                val password = etPassword.text.toString()
                if (password.isEmpty()) {
                    Toast.makeText(this@WithdrawActivity, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                    return@setOnSingleClickListener
                }

                userViewModel.deleteAccount(password) { success, message ->
                    if (success) {
                        Toast.makeText(this@WithdrawActivity, "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@WithdrawActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            tvCancel.setOnSingleClickListener {
                finish()
            }
        }
    }

    override fun getViewBinding(): ActivityWithdrawBinding {
        return ActivityWithdrawBinding.inflate(layoutInflater)
    }
}