package com.ljystamp.feature_auth.presentation.view

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.ljystamp.core_ui.theme.AppColors
import com.ljystamp.core_ui.theme.AppTypography

@Composable
fun ResetPasswordScreen(
    navController: NavController
){
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "비밀번호 찾기",
            style = AppTypography.fontSize20ExtraBold,
            modifier = Modifier.padding(top = 20.dp, start = 20.dp)
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 16.dp, end = 16.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(AppColors.ColorFF8C00)
                .clickable {
                   if(email.isEmpty()) {
                       Toast.makeText(context, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
                       return@clickable
                   }

                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(context, "유효한 이메일 주소를 입력해주세요", Toast.LENGTH_SHORT).show()
                        return@clickable
                    }

                    auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context,
                                    "비밀번호 재설정 이메일을 발송했습니다. 이메일을 확인해주세요.",
                                    Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            } else {
                                Toast.makeText(context, "비밀번호 재설정 이메일 발송에 실패했습니다: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "비밀번호 재설정 이메일 받기",
                style = AppTypography.fontSize16Regular,
                textAlign = TextAlign.Center
            )
        }
    }
}
