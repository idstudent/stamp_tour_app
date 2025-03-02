package com.ljystamp.feature_auth.presentation.view

import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.ljystamp.core_ui.BaseActivity
import com.ljystamp.feature_auth.databinding.ActivitySignUpBinding
import com.ljystamp.feature_auth.presentation.viewmodel.AuthViewModel
import com.ljystamp.utils.setOnSingleClickListener


class SignUpActivity: BaseActivity<ActivitySignUpBinding>() {
    private val signUpViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupTextChangeListeners()
        setupSignUpButton()
    }

    private fun setupTextChangeListeners() {
        binding.run {
            etEmail.addTextChangedListener {
                binding.tilEmail.error = null
            }

            etPassword.addTextChangedListener {
                binding.tilPassword.error = null
            }

            etNickname.addTextChangedListener {
                binding.tilNickname.error = null
            }
        }
    }

    private fun setupSignUpButton() {
        binding.run {
            buttonSignUp.setOnSingleClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val nickname = etNickname.text.toString()

                var hasError = false

                if (email.isEmpty()) {
                    tilEmail.error = "이메일을 입력해주세요"
                    hasError = true
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    tilEmail.error = "올바른 이메일 형식이 아닙니다"
                    hasError = true
                }

                if (password.isEmpty()) {
                    tilPassword.error = "비밀번호를 입력해주세요"
                    hasError = true
                } else if (password.length < 6) {
                    tilPassword.error = "비밀번호는 6자 이상이어야 합니다"
                    hasError = true
                }

                if (nickname.isEmpty()) {
                    tilNickname.error = "닉네임을 입력해주세요"
                    hasError = true
                }

                if (hasError) {
                    return@setOnSingleClickListener
                }

                signUpViewModel.signUp(email, password, nickname) { success, message ->
                    if (success) {
                        tvCompleteSignup.visibility = View.VISIBLE
                        tvCompleteSignup.setTextColor(Color.parseColor("#4CAF50"))
                        tvCompleteSignup.text = "회원가입이 완료되었어요.\n가입하신 이메일로 인증을 완료해야 회원가입이 완료됩니다."
                    } else {
                        tvCompleteSignup.visibility = View.VISIBLE
                        tvCompleteSignup.setTextColor(Color.RED)
                        tvCompleteSignup.text = "회원가입이 실패했어요.\n$message"
                    }
                }
            }
        }
    }

    override fun getViewBinding(): ActivitySignUpBinding {
        return ActivitySignUpBinding.inflate(layoutInflater)
    }
}