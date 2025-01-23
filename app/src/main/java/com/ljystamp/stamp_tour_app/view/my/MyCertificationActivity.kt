package com.ljystamp.stamp_tour_app.view.my

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.flexbox.FlexboxLayout
import com.ljystamp.stamp_tour_app.api.model.SavedLocation
import com.ljystamp.stamp_tour_app.databinding.ActivityMyCertificationBinding
import com.ljystamp.stamp_tour_app.view.BaseActivity

class MyCertificationActivity: BaseActivity<ActivityMyCertificationBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun initView() {
        val completeTourList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 (API 33) 이상
            intent.getParcelableArrayListExtra("tourList", SavedLocation::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra("tourList")
        } ?: arrayListOf()

        val completeCultureList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 (API 33) 이상
            intent.getParcelableArrayListExtra("cultureList", SavedLocation::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra("cultureList")
        } ?: arrayListOf()

        Log.e("ljy", "뭐야 이벤트 $completeCultureList")

        val completeEventList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 (API 33) 이상
            intent.getParcelableArrayListExtra("eventList", SavedLocation::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra("eventList")
        } ?: arrayListOf()

        val completeActivityList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 (API 33) 이상
            intent.getParcelableArrayListExtra("activityList", SavedLocation::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra("activityList")
        } ?: arrayListOf()

        val completeFoodList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 (API 33) 이상
            intent.getParcelableArrayListExtra("foodList", SavedLocation::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra("foodList")
        } ?: arrayListOf()

        binding.run {
            // 여행지
            when (completeTourList.filter { it.isVisited }.size) {
                in 0..9 -> {
                    flTour.visibility = View.GONE
                }
                in 10..29 -> {
                    visibleUnder30(flTour, llTourBadge1, llTourBadge2, llTourBadge3)
                }
                in 30..49 -> {
                    visibleUnder50(flTour, llTourBadge1, llTourBadge2, llTourBadge3)
                }
                else -> {
                    visibleOver50(flTour, llTourBadge1, llTourBadge2, llTourBadge3)
                }
            }

            // 문화
            when(completeCultureList.filter { it.isVisited }.size) {
                in 0..9 -> {
                    flCulture.visibility = View.GONE
                }
                in 10..29 -> {
                    visibleUnder30(flCulture, llCultureBadge1, llCultureBadge2, llCultureBadge3)
                }
                in 30..49 -> {
                    visibleUnder50(flCulture, llCultureBadge1, llCultureBadge2, llCultureBadge3,)
                }
                else -> {
                    visibleOver50(flCulture, llCultureBadge1, llCultureBadge2, llCultureBadge3)
                }
            }

            // 축제
            when(completeEventList.filter { it.isVisited }.size) {
                in 0..9 -> {
                    flEvent.visibility = View.GONE
                }
                in 10..29 -> {
                    visibleUnder30(flEvent, llEventBadge1, llEventBadge2, llEventBadge3)
                }
                in 30..49 -> {
                    visibleUnder50(flEvent, llEventBadge1, llEventBadge2, llEventBadge3)
                }
                else -> {
                    visibleOver50(flEvent, llEventBadge1, llEventBadge2, llEventBadge3)
                }
            }

            // 액티비티
            when(completeActivityList.filter { it.isVisited }.size) {
                in 0..9 -> {
                    flActivity.visibility = View.GONE
                }
                in 10..29 -> {
                    visibleUnder30(flActivity, llActivityBadge1, llActivityBadge2, llActivityBadge3)
                }
                in 30..49 -> {
                    visibleUnder50(flActivity, llActivityBadge1, llActivityBadge2, llActivityBadge3)
                }
                else -> {
                    visibleOver50(flActivity, llActivityBadge1, llActivityBadge2, llActivityBadge3)
                }
            }

            // 음식
            when(completeFoodList.filter { it.isVisited }.size) {
                in 0..9 -> {
                    flFood.visibility = View.GONE
                }
                in 10..29 -> {
                    visibleUnder30(flFood, llFoodBadge1, llFoodBadge2, llFoodBadge3)
                }
                in 30..49 -> {
                    visibleUnder50(flFood, llFoodBadge1, llFoodBadge2, llFoodBadge3)
                }
                else -> {
                    visibleOver50(flFood, llFoodBadge1, llFoodBadge2, llFoodBadge3)
                }
            }
        }
    }

    private fun visibleUnder30(
        fl: FlexboxLayout, ll1: LinearLayoutCompat,
        ll2: LinearLayoutCompat, ll3: LinearLayoutCompat) {

        fl.visibility = View.VISIBLE

        ll1.visibility = View.VISIBLE
        ll2.visibility = View.GONE
        ll3.visibility = View.GONE
    }

    private fun visibleUnder50(
        fl: FlexboxLayout, ll1: LinearLayoutCompat,
        ll2: LinearLayoutCompat, ll3: LinearLayoutCompat) {

        fl.visibility = View.VISIBLE

        ll1.visibility = View.VISIBLE
        ll2.visibility = View.VISIBLE
        ll3.visibility = View.GONE
    }

    private fun visibleOver50(
        fl: FlexboxLayout, ll1: LinearLayoutCompat,
        ll2: LinearLayoutCompat, ll3: LinearLayoutCompat) {

        fl.visibility = View.VISIBLE

        ll1.visibility = View.VISIBLE
        ll2.visibility = View.VISIBLE
        ll3.visibility = View.VISIBLE
    }
    override fun getViewBinding(): ActivityMyCertificationBinding {
        return ActivityMyCertificationBinding.inflate(layoutInflater)
    }
}