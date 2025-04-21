package com.ljystamp.feature_auth.presentation.view

import android.util.Patterns
import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ljystamp.core_ui.theme.AppTypography
import com.ljystamp.feature_auth.presentation.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var nicknameError by remember { mutableStateOf<String?>(null) }

    var signUpMessage by remember { mutableStateOf<Pair<String, Boolean>?>(null) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "회원가입",
            style = AppTypography.fontSize20ExtraBold,
            modifier = Modifier.padding(top = 20.dp, start = 20.dp)
        )

        TextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null
            },
            placeholder = { Text("이메일", color = Color.White.copy(alpha = 0.7f)) },
            textStyle = AppTypography.fontSize16Regular,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = if (emailError != null) Color(0xFFFF4444) else Color.White,
                unfocusedIndicatorColor = if (emailError != null) Color(0xFFFF4444) else Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError != null,
            supportingText = {
                emailError?.let {
                    Text(
                        text = it,
                        color = Color(0xffff4444),
                        style = TextStyle(fontSize = 12.sp)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 20.dp, end = 20.dp)
        )

        TextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
            },
            placeholder = { Text("비밀번호 (6자 이상)", color = Color.White.copy(alpha = 0.7f)) },
            textStyle = AppTypography.fontSize16Regular.copy(color = Color.White),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = if (passwordError != null) Color(0xFFFF4444) else Color.White,
                unfocusedIndicatorColor = if (passwordError != null) Color(0xFFFF4444) else Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null,
            supportingText = {
                passwordError?.let {
                    Text(
                        text = it,
                        color = Color(0xFFFF4444),
                        style = TextStyle(fontSize = 12.sp)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 20.dp, end = 20.dp)
        )

        TextField(
            value = nickname,
            onValueChange = {
                nickname = it
                nicknameError = null
            },
            placeholder = { Text("닉네임", color = Color.White.copy(alpha = 0.7f)) },
            textStyle = AppTypography.fontSize16Regular,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = if (nicknameError != null) Color(0xFFFF4444) else Color.White,
                unfocusedIndicatorColor = if (nicknameError != null) Color(0xFFFF4444) else Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            isError = nicknameError != null,
            supportingText = {
                nicknameError?.let {
                    Text(
                        text = it,
                        color = Color(0xFFFF4444),
                        style = TextStyle(fontSize = 12.sp)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 20.dp, end = 20.dp)
        )

        AnimatedVisibility(
            visible = signUpMessage != null,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            signUpMessage?.let { (message, isSuccess) ->
                Text(
                    text = message,
                    color = if (isSuccess) Color(0xFF4CAF50) else Color(0xFFFF0000),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Button(
            onClick = {
                var hasError = false

                if (email.isEmpty()) {
                    emailError = "이메일을 입력해주세요"
                    hasError = true
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailError = "올바른 이메일 형식이 아닙니다"
                    hasError = true
                }

                if (password.isEmpty()) {
                    passwordError = "비밀번호를 입력해주세요"
                    hasError = true
                } else if (password.length < 6) {
                    passwordError = "비밀번호는 6자 이상이어야 합니다"
                    hasError = true
                }

                if (nickname.isEmpty()) {
                    nicknameError = "닉네임을 입력해주세요"
                    hasError = true
                }

                if (hasError) {
                    return@Button
                }

                authViewModel.signUp(email, password, nickname) { success, message ->
                    if (success) {
                        signUpMessage = Pair(
                            "회원가입이 완료되었어요.\n가입하신 이메일로 인증을 완료해야 회원가입이 완료됩니다.",
                            true
                        )
                    } else {
                        signUpMessage = Pair(
                            "회원가입이 실패했어요.\n$message",
                            false
                        )
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF8C00)
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 20.dp, end = 20.dp)
                .height(48.dp)

        ) {
            Text(
                text = "회원가입",
                style = AppTypography.fontSize16Regular,
                textAlign = TextAlign.Center
            )
        }
    }
}