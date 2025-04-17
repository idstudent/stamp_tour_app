package com.ljystamp.feature_my.presentation.view

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.ljystamp.core_ui.theme.AppTypography

@Composable
fun PrivacyScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "개인정보 처리방침",
            style = AppTypography.fontSize20ExtraBold,
            modifier = Modifier.padding(top = 20.dp, start = 20.dp)
        )

        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        setSupportZoom(true)
                    }
                    webViewClient = WebViewClient()
                    loadUrl("https://blog.naver.com/sm1374/223704797394")
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        )
    }
}