package com.ljystamp.stamp_tour_app.view.user

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.ljystamp.stamp_tour_app.databinding.ActivityResetPasswordBinding
import com.ljystamp.stamp_tour_app.util.setOnSingleClickListener

class ResetPasswordActivity: BaseActivity<ActivityResetPasswordBinding>() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initEmailInput()
    }

    private fun initEmailInput() {
        binding.run {
            btnReset.setOnSingleClickListener {
                val email = etEmail.text.toString()

                // 이메일 입력 확인
                if (email.isEmpty()) {
                    showError("이메일을 입력해주세요")
                    return@setOnSingleClickListener
                }

                // 이메일 형식 검사
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    showError("유효한 이메일 주소를 입력해주세요")
                    return@setOnSingleClickListener
                }

                // 비밀번호 재설정 이메일 발송
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@ResetPasswordActivity,
                                "비밀번호 재설정 이메일을 발송했습니다. 이메일을 확인해주세요.",
                                Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            showError("비밀번호 재설정 이메일 발송에 실패했습니다: ${task.exception?.message}")
                        }
                    }
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun getViewBinding(): ActivityResetPasswordBinding {
        return ActivityResetPasswordBinding.inflate(layoutInflater)
    }
}