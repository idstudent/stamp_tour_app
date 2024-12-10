package com.ljystamp.stamp_tour_app.view.user

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import com.ljystamp.stamp_tour_app.databinding.ActivitySignUpBinding
import com.ljystamp.stamp_tour_app.util.setOnSingleClickListener
import com.ljystamp.stamp_tour_app.view.BaseActivity
import com.ljystamp.stamp_tour_app.viewmodel.SignUpViewModel


class SignUpActivity: BaseActivity<ActivitySignUpBinding>() {
    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.buttonSignUp.setOnSingleClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val nickname = binding.etNickname.text.toString()

            // 입력값 검증
            if (email.isEmpty() || password.isEmpty() || nickname.isEmpty()) {
                Toast.makeText(this, "모든 필드를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnSingleClickListener
            }

            // 비밀번호 유효성 검사 (최소 6자 이상)
            if (password.length < 6) {
                Toast.makeText(this, "비밀번호는 6자 이상이어야 합니다", Toast.LENGTH_SHORT).show()
                return@setOnSingleClickListener
            }

            // 이메일 형식 검사
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "유효한 이메일 주소를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnSingleClickListener
            }

            // 닉네임 유효성 검사 추가하고 싶다면 여기에 추가

            // 회원가입 진행 (닉네임 중복 체크 포함)
            signUpViewModel.signUp(email, password, nickname) { success, message ->
                if (success) {
                    Toast.makeText(this, "회원가입이 완료되었습니다. 이메일 인증을 진행해주세요.", Toast.LENGTH_SHORT).show()
                    finish() // 로그인 화면으로 돌아가기
                } else {
                    Toast.makeText(this, "회원가입 실패: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getViewBinding(): ActivitySignUpBinding {
        return ActivitySignUpBinding.inflate(layoutInflater)
    }
}