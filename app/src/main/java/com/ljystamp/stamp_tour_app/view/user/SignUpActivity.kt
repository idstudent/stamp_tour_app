package com.ljystamp.stamp_tour_app.view.user

import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.ljystamp.stamp_tour_app.databinding.ActivitySignUpBinding
import com.ljystamp.stamp_tour_app.util.setOnSingleClickListener
import com.ljystamp.stamp_tour_app.view.BaseActivity
import com.ljystamp.stamp_tour_app.viewmodel.UserViewModel


class SignUpActivity: BaseActivity<ActivitySignUpBinding>() {
    private val signUpViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupTextChangeListeners()
        setupSignUpButton()
    }

    private fun setupTextChangeListeners() {
        // 이메일 입력 텍스트 변경 리스너
        binding.etEmail.addTextChangedListener {
            binding.tilEmail.error = null
        }

        // 비밀번호 입력 텍스트 변경 리스너
        binding.etPassword.addTextChangedListener {
            binding.tilPassword.error = null
        }

        // 닉네임 입력 텍스트 변경 리스너
        binding.etNickname.addTextChangedListener {
            binding.tilNickname.error = null
        }
    }

    private fun setupSignUpButton() {
        binding.buttonSignUp.setOnSingleClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val nickname = binding.etNickname.text.toString()

            // 입력값 검증
            var hasError = false

            if (email.isEmpty()) {
                binding.tilEmail.error = "이메일을 입력해주세요"
                hasError = true
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilEmail.error = "올바른 이메일 형식이 아닙니다"
                hasError = true
            }

            if (password.isEmpty()) {
                binding.tilPassword.error = "비밀번호를 입력해주세요"
                hasError = true
            } else if (password.length < 6) {
                binding.tilPassword.error = "비밀번호는 6자 이상이어야 합니다"
                hasError = true
            }

            if (nickname.isEmpty()) {
                binding.tilNickname.error = "닉네임을 입력해주세요"
                hasError = true
            }

            if (hasError) {
                return@setOnSingleClickListener
            }

            // 회원가입 진행
            signUpViewModel.signUp(email, password, nickname) { success, message ->
                if (success) {
                    binding.tvCompleteSignup.visibility = View.VISIBLE
                    binding.tvCompleteSignup.setTextColor(Color.parseColor("#4CAF50"))
                    binding.tvCompleteSignup.text = "회원가입이 완료되었어요.\n가입하신 이메일로 인증을 완료해야 회원가입이 완료됩니다."
                } else {
                    binding.tvCompleteSignup.visibility = View.VISIBLE
                    binding.tvCompleteSignup.setTextColor(Color.RED)
                    binding.tvCompleteSignup.text = "회원가입이 실패했어요.\n$message"
                }
            }
        }
    }

    override fun getViewBinding(): ActivitySignUpBinding {
        return ActivitySignUpBinding.inflate(layoutInflater)
    }
}