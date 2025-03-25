package com.ljystamp.feature_my_tour_detail.presentation.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.ljystamp.common.presentation.viewmodel.LocationTourListViewModel
import com.ljystamp.core_ui.BaseActivity
import com.ljystamp.feature_my_tour_detail.databinding.ActivityMyTourDetailBinding
import com.ljystamp.feature_tour_detail.presentation.viewmodel.TourDetailViewModel
import com.ljystamp.stamp_tour_app.model.SavedLocation
import com.ljystamp.utils.removeHtmlTags
import com.ljystamp.utils.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyTourDetailActivity: BaseActivity<ActivityMyTourDetailBinding>() {
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val tourDetailViewModel: TourDetailViewModel by viewModels()
    private val locationTourListViewModel: LocationTourListViewModel by viewModels()

    private var isLocationPermissionGranted = false

    var tourInfo: SavedLocation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initListener()
        checkLocationPermission()
    }

    private fun initView() {
        val intent = intent

        tourInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("info", SavedLocation::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("info")
        }


        tourInfo?.let { tourInfo ->
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    tourDetailViewModel.getTourDetail(tourInfo.contentId, tourInfo.contentTypeId)
                    tourDetailViewModel.tourDetailInfo.collect {
                        binding.run {
                            if(it.isNotEmpty()) {
                                it[0].run {

                                    Glide.with(binding.root.context)
                                        .load(tourInfo.image)
                                        .into(ivThumb)

                                    tvTitle.text = tourInfo.title
                                    tvAddr.text = tourInfo.address

                                    when(tourInfo.contentTypeId) {
                                        12 -> {
                                            gpTourPlace.visibility = View.VISIBLE
                                            gpCulture.visibility = View.GONE
                                            gpEvent.visibility = View.GONE
                                            gpActivity.visibility = View.GONE
                                            gpFood.visibility = View.GONE

                                            tvVisible(tvOpenDate, this.openDate)
                                            tvOpenDate.text = "개장일: ${this.openDate}".removeHtmlTags()

                                            tvVisible(tvRestDate, this.restDate)
                                            tvRestDate.text = "휴무일: ${this.restDate ?: ""}".removeHtmlTags()

                                            tvVisible(tvUseTime, this.useTime)
                                            tvUseTime.text = "이용 가능 시간: ${this.useTime ?: ""}".removeHtmlTags()
                                        }

                                        14 -> {
                                            gpTourPlace.visibility = View.GONE
                                            gpCulture.visibility = View.VISIBLE
                                            gpEvent.visibility = View.GONE
                                            gpActivity.visibility = View.GONE
                                            gpFood.visibility = View.GONE

                                            tvVisible(tvCultureRestDate, this.cultureRestDate)
                                            tvCultureRestDate.text = "휴무일: ${this.cultureRestDate}".removeHtmlTags()

                                            tvVisible(tvCultureUseDate, this.cultureUseTime)
                                            tvCultureUseDate.text = "이용 가능 시간: ${this.cultureUseTime}".removeHtmlTags()

                                            tvVisible(tvCulturePrice, this.culturePrice)
                                            tvCulturePrice.text = "이용 요금: ${this.culturePrice}".removeHtmlTags()

                                            tvVisible(tvCultureParking, this.cultureParking)
                                            tvCultureParking.text = "주차: ${this.cultureParking}".removeHtmlTags()

                                            tvVisible(tvCultureParkingPrice, this.cultureParkingFee)
                                            tvCultureParkingPrice.text = "주차 요금: ${this.cultureParkingFee}".removeHtmlTags()

                                            tvVisible(tvCultureInfo, this.cultureInfoCenter)
                                            tvCultureInfo.text = "문의: ${this.cultureInfoCenter}".removeHtmlTags()
                                        }

                                        15 -> {
                                            gpTourPlace.visibility = View.GONE
                                            gpCulture.visibility = View.GONE
                                            gpEvent.visibility = View.VISIBLE
                                            gpActivity.visibility = View.GONE
                                            gpFood.visibility = View.GONE

                                            tvVisible(tvEventStartDate, this.eventStartDate)
                                            tvEventStartDate.text = "행사 시작일: ${this.eventStartDate}".removeHtmlTags()

                                            tvVisible(tvEventEndDate, this.eventEndDate)
                                            tvEventEndDate.text = "행사 종료일: ${this.eventEndDate}".removeHtmlTags()

                                            tvVisible(tvEventPlayTime, this.eventPlayTime)
                                            tvEventPlayTime.text = "행사 시간: ${this.eventPlayTime}".removeHtmlTags()

                                            tvVisible(tvEventPlace, this.eventPlace)
                                            tvEventPlace.text = "장소: ${this.eventPlace}".removeHtmlTags()

                                            tvVisible(tvEventPrice, this.eventUsePrice)
                                            tvEventPrice.text = "이용 금액: ${this.eventUsePrice}".removeHtmlTags()

                                            tvVisible(tvEventSponsor, this.eventSponsor)
                                            tvEventSponsor.text = "주최자: ${this.eventSponsor}".removeHtmlTags()

                                            tvVisible(tvEventSponsorInfo, this.eventSponsorTel)
                                            tvEventSponsorInfo.text = "주최자 문의: ${this.eventSponsorTel}".removeHtmlTags()
                                        }

                                        28 -> {
                                            gpTourPlace.visibility = View.GONE
                                            gpCulture.visibility = View.GONE
                                            gpEvent.visibility = View.GONE
                                            gpActivity.visibility = View.VISIBLE
                                            gpFood.visibility = View.GONE

                                            tvVisible(tvActivityEndDate, this.activityRestDate)
                                            tvActivityEndDate.text = "휴무일: ${this.activityRestDate}".removeHtmlTags()

                                            tvVisible(tvActivityPlayTime, this.activityUseTime)
                                            tvActivityPlayTime.text = "이용 가능 시간: ${this.activityUseTime}".removeHtmlTags()

                                            tvVisible(tvActivityAge, this.activityPossibleAge)
                                            tvActivityAge.text = "이용 가능 연령: ${this.activityPossibleAge}".removeHtmlTags()

                                            tvVisible(tvActivityParking, this.activityParking)
                                            tvActivityParking.text = "주차 및 요금: ${this.activityParking}".removeHtmlTags()

                                            tvVisible(tvActivityReservation, this.activityReservation)
                                            tvActivityReservation.text = "예약 안내: ${this.activityReservation}".removeHtmlTags()

                                            tvVisible(tvActivityInfo, this.activityInfoCenter)
                                            tvActivityInfo.text = "문의: ${this.activityInfoCenter}".removeHtmlTags()
                                        }

                                        39 -> {
                                            gpTourPlace.visibility = View.GONE
                                            gpCulture.visibility = View.GONE
                                            gpEvent.visibility = View.GONE
                                            gpActivity.visibility = View.GONE
                                            gpFood.visibility = View.VISIBLE

                                            tvVisible(tvFoodRestDate, this.foodRestTime)
                                            tvFoodRestDate.text = "휴무일: ${this.foodRestTime}".removeHtmlTags()

                                            tvVisible(tvFoodOpenDate, this.foodOpenTime)
                                            tvFoodOpenDate.text = "영업 시간: ${this.foodOpenTime}".removeHtmlTags()

                                            tvVisible(tvFoodFirstMenu, this.foodFirstMenu)
                                            tvFoodFirstMenu.text = "대표 메뉴: ${this.foodFirstMenu}".removeHtmlTags()

                                            tvVisible(tvFoodTreatMenu, this.foodTreatMenu)
                                            tvFoodTreatMenu.text = "메뉴: ${this.foodTreatMenu}".removeHtmlTags()

                                            tvVisible(tvFoodTakeOut, this.foodTakeOut)
                                            tvFoodTakeOut.text = "포장: ${this.foodTakeOut}".removeHtmlTags()

                                            tvVisible(tvFoodInfo, this.foodInfoCenter)
                                            tvFoodInfo.text = "문의: ${this.foodInfoCenter}".removeHtmlTags()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun tvVisible(textView: AppCompatTextView, data: String?) {
        textView.isVisible = data != "" && data != null
    }
    private fun checkLocationPermission() {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    if (ActivityCompat.checkSelfPermission(
                            this@MyTourDetailActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED) {
                        isLocationPermissionGranted = true
                    } else {
                        isLocationPermissionGranted = false
                        Toast.makeText(
                            this@MyTourDetailActivity,
                            "정확한 위치 확인을 위해 '정확한 위치' 권한을 허용해주세요",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onPermissionDenied(deniedPermissions: List<String>) {
                    isLocationPermissionGranted = false
                    Toast.makeText(
                        this@MyTourDetailActivity,
                        "위치 권한이 거부되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
            .setDeniedMessage("정확한 위치 권한을 받지 않으면 몇몇 기능을 사용하지 못해요!\n정확한 위치를 켜시려면 설정 > 권한 > 위치 > 정확한 위치사용을 켜주세요")
            .setPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .check()
    }

    private fun initListener() {
        binding.btnComplete.setOnSingleClickListener {
            if(isLocationPermissionGranted) {
                tourInfo?.let { savedLocation ->
                    try {
                        fusedLocationClient.lastLocation
                            .addOnSuccessListener { location ->
                                location?.let {
                                    val results = FloatArray(1)
                                    Location.distanceBetween(
                                        it.latitude,
                                        it.longitude,
                                        savedLocation.latitude,
                                        savedLocation.longitude,
                                        results
                                    )

                                    val distanceInMeters = results[0]
                                    if (distanceInMeters <= 300) {
                                        locationTourListViewModel.updateVisitStatus(savedLocation.contentId) { success, message ->
                                            message?.let { msg ->
                                                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "해당 장소와의 거리가 너무 멀어요! (${
                                                String.format(
                                                    "%.1f",
                                                    distanceInMeters
                                                )
                                            }m)",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } ?: run {
                                    Toast.makeText(
                                        this,
                                        "위치 정보를 가져올 수 없어요.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } catch (e: SecurityException) {
                        Toast.makeText(
                            this,
                            "위치 권한이 없습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }else {
                Toast.makeText(this, "위치 권한이 필요해요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun getViewBinding(): ActivityMyTourDetailBinding {
        return ActivityMyTourDetailBinding.inflate(layoutInflater)
    }
}
