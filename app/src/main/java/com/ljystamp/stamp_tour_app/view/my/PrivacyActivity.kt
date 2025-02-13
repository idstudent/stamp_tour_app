package com.ljystamp.stamp_tour_app.view.my

import android.os.Bundle
import android.webkit.WebViewClient
import com.ljystamp.stamp_tour_app.databinding.ActivityPrivacyBinding

class PrivacyActivity: BaseActivity<ActivityPrivacyBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 웹뷰 설정
        binding.wvPrivacy.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            setSupportZoom(true)
        }

        binding.wvPrivacy.webViewClient = WebViewClient() // 기본 웹뷰 클라이언트 설정
        binding.wvPrivacy.loadUrl("https://blog.naver.com/sm1374/223704797394")
    }

    override fun getViewBinding(): ActivityPrivacyBinding {
        return ActivityPrivacyBinding.inflate(layoutInflater)
    }
}