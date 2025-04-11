package com.ljystamp.feature_my.presentation.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ljystamp.core_ui.theme.AppTypography

@Composable
fun SettingScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "설정",
            style = AppTypography.fontSize20ExtraBold,
            modifier = Modifier.padding(top = 20.dp, start =  20.dp)
        )

        Text(
            text = "개인정보 처리방침",
            style = AppTypography.fontSize16Regular,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .padding(top = 28.dp)
                .clickable {  }
        )

        Text(
            text = "회원탈퇴",
            style = AppTypography.fontSize16Regular,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .clickable {  }
        )

        Text(
            text = "문의: sm1374@naver.com",
            style = AppTypography.fontSize16Regular,
            modifier = Modifier
                .padding(top = 16.dp, start = 20.dp)
        )
    }
}