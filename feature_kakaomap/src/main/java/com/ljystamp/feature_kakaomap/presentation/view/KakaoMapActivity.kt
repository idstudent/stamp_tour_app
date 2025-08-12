package com.ljystamp.feature_kakaomap.presentation.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.ljystamp.core_ui.BaseActivity
import com.ljystamp.feature_kakaomap.databinding.ActivityKakaoMapBinding
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextBuilder
import com.kakao.vectormap.label.LabelTextStyle
import com.ljystamp.feature_kakaomap.R
import com.ljystamp.stamp_tour_app.model.TourMapper

class KakaoMapActivity: BaseActivity<ActivityKakaoMapBinding>() {
    private var kakaoMap: KakaoMap? = null
    private val labelManager by lazy { kakaoMap?.labelManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initKakaoMap()
    }

    private fun initKakaoMap() {
        val intent = intent
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        val nearTourList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 (API 33) 이상
            intent.getParcelableArrayListExtra("tourList", TourMapper::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra("tourList")
        } ?: arrayListOf()

        binding.mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.e("ljy", "destory")
            }

            override fun onMapError(error: Exception) {
                Log.e("ljy", "카카오 에러: $error")
                error.printStackTrace()
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
                kakaoMap = map

                val myPosition = LatLng.from(latitude, longitude)
                kakaoMap?.moveCamera(
                    CameraUpdateFactory.newCenterPosition(myPosition, 11)
                )

                nearTourList.forEach {
                    Log.e("ljy", "리스트 ${it.title} ${it.mapY} ${it.mapX}")
                    addTouristMarker(it.title, it.mapY, it.mapX)
                }

                kakaoMap?.setOnLabelClickListener { kakaoMap, layer, label ->
                    val labelId = label.labelId
                    Log.d("ljy", "클릭된 관광지: $labelId")

                    true
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        binding.mapView.resume()
    }

    private fun addTouristMarker(name: String, lat: Double, lng: Double) {
        val position = LatLng.from(lat, lng)

        Log.e("ljy", "마커 추가 시도: $name, 위도: $lat, 경도: $lng")

        val touristStyles = kakaoMap?.labelManager?.addLabelStyles(
            LabelStyles.from(
                LabelStyle.from()
                    .setTextStyles(LabelTextStyle.from(48, android.graphics.Color.RED))
                    .setZoomLevel(1)
            )
        )

        val options = LabelOptions.from(name, position)
            .setStyles(touristStyles)
            .setTexts(LabelTextBuilder().setTexts("📍"))

        val layer = kakaoMap?.labelManager?.layer
        layer?.addLabel(options)
    }

    override fun onPause() {
        super.onPause()

        binding.mapView.pause()
    }

    override fun getViewBinding(): ActivityKakaoMapBinding {
        return ActivityKakaoMapBinding.inflate(layoutInflater)
    }
}