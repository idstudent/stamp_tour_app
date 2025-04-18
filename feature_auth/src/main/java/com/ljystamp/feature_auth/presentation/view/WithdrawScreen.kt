package com.ljystamp.feature_auth.presentation.view

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ljystamp.core_ui.theme.AppColors
import com.ljystamp.core_ui.theme.AppTypography
import com.ljystamp.feature_auth.presentation.viewmodel.AuthViewModel
import kotlin.system.exitProcess

@Composable
fun WithdrawScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    var password by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "회원탈퇴",
                style = AppTypography.fontSize20ExtraBold,
                modifier = Modifier.padding(top = 20.dp, start = 20.dp)
            )

            Text(
                text = "회원탈퇴를 하기 위해\n비밀번호 확인이 필요해요.",
                style = AppTypography.fontSize20ExtraBold,
                modifier = Modifier.padding(top = 16.dp, start = 20.dp)
            )

            TextField(
                value = password,
                onValueChange = { password = it },
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
                    .padding(top = 24.dp, start = 20.dp, end = 20.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 48.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(
                text = "탈퇴하기",
                style = AppTypography.fontSize16Regular.copy(color = AppColors.Black),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(vertical = 16.dp)
                    .clickable {
                        if(password.isEmpty()) {
                            Toast.makeText(context, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                            return@clickable
                        }

                        authViewModel.deleteAccount(password) { success, message ->
                            if(success) {
                                Toast.makeText(context, "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                                val activity = context as? Activity
                                activity?.finishAffinity()

                                exitProcess(0)
                            }else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            )

            Text(
                text = "탈퇴 취소",
                style = AppTypography.fontSize16ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .padding(vertical = 16.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
        }
    }
}