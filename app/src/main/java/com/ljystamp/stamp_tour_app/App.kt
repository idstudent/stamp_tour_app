package com.ljystamp.stamp_tour_app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.kakao.vectormap.KakaoMapSdk

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoMapSdk.init(this, "cdbb0d92a4d048235c6f68acad13021b")
    }
}