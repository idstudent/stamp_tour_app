package com.ljystamp.feature_tour_detail.presentation.view

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.ljystamp.common.presentation.viewmodel.LocationTourListViewModel
import com.ljystamp.core_ui.BaseActivity
import com.ljystamp.feature_tour_detail.databinding.ActivityTourDetailBinding
import com.ljystamp.stamp_tour_app.model.SaveResult
import com.ljystamp.stamp_tour_app.model.TourMapper
import com.ljystamp.utils.removeHtmlTags
import com.ljystamp.utils.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.ljystamp.core_ui.R
import com.ljystamp.feature_tour_detail.presentation.viewmodel.TourDetailViewModel

@AndroidEntryPoint
class TourDetailActivity: BaseActivity<ActivityTourDetailBinding>() {
    private val tourDetailViewModel: TourDetailViewModel by viewModels()
    private val locationTourListViewModel: LocationTourListViewModel by viewModels()

    private var tourInfo: TourMapper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initListener()
    }

    private fun initView() {
        val intent = intent
        tourInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("info", TourMapper::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("info")
        }

        val enterSearch = intent.getBooleanExtra("search", false)

        tourInfo?.let {
            tourDetailViewModel.getTourDetail(it.contentId, it.contentTypeId)

            if(enterSearch) {
                tourDetailViewModel.insertItem(it)
            }

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    tourDetailViewModel.tourDetailInfo.collect { detailInfo ->
                        binding.run {
                            if(detailInfo.isNotEmpty()) {
                                detailInfo[0].run {
                                    Glide.with(binding.root.context)
                                        .load(it.firstImage)
                                        .into(ivThumb)

                                    tvTitle.text = it.title
                                    tvAddr.text = it.addr1

                                    when(it.contentTypeId) {
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

                                isSavedCheck()
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

    private fun initListener() {
        binding.btnComplete.setOnSingleClickListener {
            tourInfo?.let { info ->
                locationTourListViewModel.saveTourLocation(info) { result ->
                    when(result) {
                        is SaveResult.Success -> {
                            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                            finish()
                        }

                        is SaveResult.Failure -> {
                            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                        }

                        is SaveResult.MaxLimitReached -> {
                            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                        }

                        is SaveResult.LoginRequired -> {
                            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
//                            handleLoginRequest()
                        }
                    }
                }
            }
        }
    }

    private fun isSavedCheck() {
        tourInfo?.contentId?.let {
            locationTourListViewModel.checkIfLocationSaved(it) { isSaved ->
                if(isSaved) {
                    binding.btnComplete.background = ContextCompat.getDrawable(binding.root.context, R.drawable.radius_12_3d3d3d)
                } else {
                    binding.btnComplete.background  = ContextCompat.getDrawable(binding.root.context, R.drawable.radius_12_ff8c00)
                }
            }
        }
    }
//    private fun handleLoginRequest() {
//        val intent = Intent(this, LoginActivity::class.java)
//        activityResultLauncher.launch(intent)
//    }

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            isSavedCheck()
        }
    }
    override fun getViewBinding(): ActivityTourDetailBinding {
        return ActivityTourDetailBinding.inflate(layoutInflater)
    }
}