package com.ljystamp.common.presentation.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ljystamp.common.presentation.viewmodel.LoginViewModel
import com.ljystamp.core_navigation.AppRoutes
import com.ljystamp.core_ui.theme.AppColors
import com.ljystamp.core_ui.theme.AppTypography

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "로그인",
            style = AppTypography.fontSize20ExtraBold,
            modifier = Modifier
                .padding(top = 20.dp, start = 20.dp)
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("이메일", color = Color.White.copy(alpha = 0.7f)) },
            textStyle = AppTypography.fontSize16Regular,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 20.dp, end = 20.dp)
        )

        TextField(
            value = password,
            onValueChange = { password = it},
            placeholder = { Text("비밀번호", color = Color.White.copy(alpha = 0.7f)) },
            textStyle = AppTypography.fontSize16Regular,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 20.dp, end = 20.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 24.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(AppColors.ColorFF8C00)
                .clickable {
                    if (email.isEmpty() || password.isEmpty()) {
                        Toast
                            .makeText(context, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_SHORT)
                            .show()
                        return@clickable
                    }

                    loginViewModel.signIn(email, password) { success, message ->
                        if (success) {
                            navController.popBackStack()
                        } else {
                            Toast
                                .makeText(context, message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "로그인",
                style = AppTypography.fontSize16Regular,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "회원가입",
                style = AppTypography.fontSize16Regular,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        navController.navigate(AppRoutes.SIGN_UP)
                    }
            )
            Text(
                text = "비밀번호 찾기",
                style = AppTypography.fontSize16Regular,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        navController.navigate(AppRoutes.RESET_PASSWORD)
                    }
            )
        }
    }
}