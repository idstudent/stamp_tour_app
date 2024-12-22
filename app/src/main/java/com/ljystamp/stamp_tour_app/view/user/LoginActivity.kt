package com.ljystamp.stamp_tour_app.view.user

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.ljystamp.stamp_tour_app.databinding.ActivityLoginBinding
import com.ljystamp.stamp_tour_app.util.setOnSingleClickListener
import com.ljystamp.stamp_tour_app.view.BaseActivity
import com.ljystamp.stamp_tour_app.viewmodel.LoginViewModel

class LoginActivity: BaseActivity<ActivityLoginBinding>() {
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initListener()
    }

    private fun initListener() {
        binding.run {
            btnLogin.setOnSingleClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                // 입력값 검증
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this@LoginActivity, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                    return@setOnSingleClickListener
                }

                loginViewModel.signIn(email, password) { success, message ->
                    if (success) {
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            tvFindPassword.setOnSingleClickListener {
                val intent = Intent(this@LoginActivity, ResetPasswordActivity::class.java)
                startActivity(intent)
            }
        }
    }
    override fun getViewBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }
}