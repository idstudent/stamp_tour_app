package com.ljystamp.feature_kakaomap.presentation.view

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
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
import com.ljystamp.common.presentation.view.LoginActivity
import com.ljystamp.common.presentation.viewmodel.LocationTourListViewModel
import com.ljystamp.stamp_tour_app.model.TourMapper
import dagger.hilt.android.AndroidEntryPoint
import com.ljystamp.core_ui.R
import com.ljystamp.stamp_tour_app.model.SaveResult

@AndroidEntryPoint
class KakaoMapActivity: BaseActivity<ActivityKakaoMapBinding>() {
    private var kakaoMap: KakaoMap? = null
    private val locationTourListViewModel: LocationTourListViewModel by viewModels()

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
            override fun onMapDestroy() {}

            override fun onMapError(error: Exception) {
                error.printStackTrace()
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
                kakaoMap = map

                val myPosition = LatLng.from(latitude, longitude)
                kakaoMap?.moveCamera(
                    CameraUpdateFactory.newCenterPosition(myPosition, 14)
                )

                nearTourList.forEach {
                    addTouristMarker(it.contentId.toString(), it.mapY, it.mapX)
                }

                kakaoMap?.setOnLabelClickListener { kakaoMap, layer, label ->
                    val labelId = label.labelId

                    binding.run {
                        emptyView.visibility = View.GONE
                        markerItem.visibility = View.VISIBLE

                        val item = nearTourList.find { it.contentId.toString() == labelId }

                        item?.let {
                            Glide.with(root.context)
                                .load(it.firstImage)
                                .transform(MultiTransformation(CenterCrop(), RoundedCorners(12)))
                                .into(ivPlaceImg)

                            tvPlace.text = it.title
                            tvAddr.text = it.addr1

                            locationTourListViewModel.checkIfLocationSaved(it.contentId) { isSaved ->
                                if(isSaved) {
                                    btnAdd.background = ContextCompat.getDrawable(binding.root.context, R.drawable.radius_12_3d3d3d)
                                } else {
                                    btnAdd.background  = ContextCompat.getDrawable(binding.root.context, R.drawable.radius_12_ff8c00)
                                }
                            }
                        }
                    }

                    true
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        binding.mapView.resume()
    }

    private fun addTouristMarker(contentId: String, lat: Double, lng: Double) {
        val position = LatLng.from(lat, lng)

        val touristStyles = kakaoMap?.labelManager?.addLabelStyles(
            LabelStyles.from(
                LabelStyle.from()
                    .setTextStyles(LabelTextStyle.from(48, android.graphics.Color.RED))
                    .setZoomLevel(1)
            )
        )

        val options = LabelOptions.from(contentId, position)
            .setStyles(touristStyles)
            .setTexts(LabelTextBuilder().setTexts("📍"))

        val layer = kakaoMap?.labelManager?.layer
        layer?.addLabel(options)
    }

    private fun handleLoginRequest() {
        val intent = Intent(this, LoginActivity::class.java)
        activityResultLauncher.launch(intent)
    }

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            binding.run {
                emptyView.visibility = View.VISIBLE
                markerItem.visibility = View.GONE
            }
        }
    }
    override fun onPause() {
        super.onPause()

        binding.mapView.pause()
    }

    override fun getViewBinding(): ActivityKakaoMapBinding {
        return ActivityKakaoMapBinding.inflate(layoutInflater)
    }
}