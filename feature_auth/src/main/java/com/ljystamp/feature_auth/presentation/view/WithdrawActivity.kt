package com.ljystamp.feature_auth.presentation.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.ljystamp.core_ui.BaseActivity
import com.ljystamp.feature_auth.databinding.ActivityWithdrawBinding
import com.ljystamp.feature_auth.presentation.viewmodel.AuthViewModel
import com.ljystamp.utils.setOnSingleClickListener

class WithdrawActivity: BaseActivity<ActivityWithdrawBinding>() {
    private val userViewModel: AuthViewModel by viewModels()
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
                        finishAffinity()  // 모든 액티비티 종료
                        System.exit(0)
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