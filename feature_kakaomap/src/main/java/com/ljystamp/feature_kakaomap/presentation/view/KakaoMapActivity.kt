package com.ljystamp.feature_kakaomap.presentation.view

import android.os.Bundle
import android.util.Log
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.ljystamp.core_ui.BaseActivity
import com.ljystamp.feature_kakaomap.databinding.ActivityKakaoMapBinding
import com.kakao.vectormap.LatLng

class KakaoMapActivity: BaseActivity<ActivityKakaoMapBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initKakaoMap()
    }

    private fun initKakaoMap() {
        val intent = intent
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)

        binding.mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.e("ljy", "destory")
            }

            override fun onMapError(error: Exception) {
                Log.e("ljy", "카카오 에러: $error")
                error.printStackTrace()
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                val position = LatLng.from(latitude, longitude)
                kakaoMap.moveCamera(
                    com.kakao.vectormap.camera.CameraUpdateFactory.newCenterPosition(position)
                )
            }
        })
    }

    override fun onResume() {
        super.onResume()
        Log.e("ljy", "onResume")
        binding.mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        Log.e("ljy", "onPause")
        binding.mapView.pause()
    }

    override fun getViewBinding(): ActivityKakaoMapBinding {
        return ActivityKakaoMapBinding.inflate(layoutInflater)
    }
}