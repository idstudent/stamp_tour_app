package com.ljystamp.common.presentation.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.ljystamp.common.databinding.ActivityLoginBinding
import com.ljystamp.common.presentation.viewmodel.LoginViewModel
import com.ljystamp.core_ui.BaseActivity
import com.ljystamp.feature_auth.presentation.view.ResetPasswordActivity
import com.ljystamp.feature_auth.presentation.view.SignUpActivity
import com.ljystamp.utils.setOnSingleClickListener

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

            tvSignUp.setOnSingleClickListener {
                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
        }
    }
    override fun getViewBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }
}