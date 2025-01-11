package com.ljystamp.stamp_tour_app.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.ljystamp.stamp_tour_app.R
import com.ljystamp.stamp_tour_app.databinding.ActivityMyTourDetailBinding
import com.ljystamp.stamp_tour_app.util.removeHtmlTags
import com.ljystamp.stamp_tour_app.util.setOnSingleClickListener
import com.ljystamp.stamp_tour_app.viewmodel.LocationTourListViewModel
import com.ljystamp.stamp_tour_app.viewmodel.TourDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyTourDetailActivity: BaseActivity<ActivityMyTourDetailBinding>() {
    private val tourDetailViewModel: TourDetailViewModel by viewModels()
    private val locationTourListViewModel: LocationTourListViewModel by viewModels()

    private var contentId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initListener()
    }

    private fun initView() {
        val intent = intent
        val title = intent.getStringExtra("title") ?: ""
        val addr = intent.getStringExtra("addr") ?: ""
        val imgUrl = intent.getStringExtra("url") ?: ""
        contentId = intent.getIntExtra("contentId", -1)
        val contentTypeId = intent.getIntExtra("contentTypeId", -1)

        if(contentId != -1 && contentTypeId != -1) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    tourDetailViewModel.getTourDetail(contentId, contentTypeId)
                    tourDetailViewModel.tourDetailInfo.collect {
                        binding.run {
                            if(it.isNotEmpty()) {
                                it[0].run {

                                    Glide.with(binding.root.context)
                                        .load(imgUrl)
                                        .into(ivThumb)

                                    tvTitle.text = title
                                    tvAddr.text = addr

                                    when(contentTypeId) {
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

    private fun initListener() {
        binding.btnComplete.setOnSingleClickListener {
            locationTourListViewModel.updateVisitStatus(contentId) { success, message ->
                Toast.makeText(binding.root.context, message, Toast.LENGTH_SHORT).show()
                if (success) {
                    finish()
                }
            }
        }
    }
    override fun getViewBinding(): ActivityMyTourDetailBinding {
        return ActivityMyTourDetailBinding.inflate(layoutInflater)
    }
}
