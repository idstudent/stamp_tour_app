package com.ljystamp.stamp_tour_app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.kakao.vectormap.KakaoMapSdk

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoMapSdk.init(this, "bf569ce2d2436be3e9d8bb2042af526d")
    }
}