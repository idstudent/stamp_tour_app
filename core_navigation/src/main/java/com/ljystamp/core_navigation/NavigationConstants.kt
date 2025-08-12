package com.ljystamp.core_navigation

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import com.ljystamp.stamp_tour_app.model.SavedLocation

object NavigationConstants {
    const val TOUR_DETAIL_ACTIVITY = "com.ljystamp.feature_tour_detail.presentation.view.TourDetailActivity"
    const val NEAR_PLACE_LIST_ACTIVITY = "com.ljystamp.feature_near_place.presentation.view.NearPlaceListActivity"
    const val MY_TOUR_LIST_ACTIVITY = "com.ljystamp.feature_my_tour.presentation.view.MyTourListActivity"
    const val SEARCH_LIST_ACTIVITY = "com.ljystamp.feature_search.presentation.view.SearchListActivity"
    const val MY_CERTIFICATION_ACTIVITY = "com.ljystamp.feature_my.presentation.view.MyCertificationActivity"
    const val MY_COMPLETE_ACTIVITY = "com.ljystamp.feature_my.presentation.view.MyCompleteListActivity"
    const val SETTING_ACTIVITY = "com.ljystamp.feature_my.presentation.view.SettingActivity"
    const val KAKAO_MAP_ACTIVITY = "com.ljystamp.feature_kakaomap.presentation.view.KakaoMapActivity"
}

object Navigator {
    fun navigateToTourDetail(context: Context, tourInfo: Parcelable) {
        val intent = Intent().setClassName(
            context.packageName,
            NavigationConstants.TOUR_DETAIL_ACTIVITY
        )
        intent.putExtra("info", tourInfo)
        context.startActivity(intent)
    }

    fun navigateToNearPlaceList(context: Context, typeId: Int) {
        val intent = Intent().setClassName(
            context.packageName,
            NavigationConstants.NEAR_PLACE_LIST_ACTIVITY
        )
        intent.putExtra("typeId", typeId)
        context.startActivity(intent)
    }
    fun navigateMyTourList(context: Context) {
        val intent = Intent().setClassName(
            context.packageName,
            NavigationConstants.MY_TOUR_LIST_ACTIVITY
        )
        context.startActivity(intent)
    }

    fun navigateSearchList(context: Context, contentTypeId: Int, keyword: String) {
        val intent = Intent().setClassName(
            context.packageName,
            NavigationConstants.SEARCH_LIST_ACTIVITY
        )
        intent.putExtra("contentTypeId", contentTypeId)
        intent.putExtra("keyword", keyword)
        context.startActivity(intent)
    }

    fun navigateMyComplete(
        context: Context, saveTourList: ArrayList<SavedLocation>,
        saveCultureList: ArrayList<SavedLocation>, saveEventList: ArrayList<SavedLocation>,
        saveActivityList: ArrayList<SavedLocation>, saveFoodList: ArrayList<SavedLocation>,
    ) {
        val intent = Intent().setClassName(
            context.packageName,
            NavigationConstants.MY_COMPLETE_ACTIVITY
        )
        intent.putExtra("tourList", saveTourList)
        intent.putExtra("cultureList", saveCultureList)
        intent.putExtra("eventList", saveEventList)
        intent.putExtra("activityList", saveActivityList)
        intent.putExtra("foodList", saveFoodList)
        context.startActivity(intent)
    }

    fun navigateMyCertification(
        context: Context, saveTourList: ArrayList<SavedLocation>,
        saveCultureList: ArrayList<SavedLocation>, saveEventList: ArrayList<SavedLocation>,
        saveActivityList: ArrayList<SavedLocation>, saveFoodList: ArrayList<SavedLocation>,
    ) {
        val intent = Intent().setClassName(
            context.packageName,
            NavigationConstants.MY_CERTIFICATION_ACTIVITY
        )
        intent.putExtra("tourList", saveTourList)
        intent.putExtra("cultureList", saveCultureList)
        intent.putExtra("eventList", saveEventList)
        intent.putExtra("activityList", saveActivityList)
        intent.putExtra("foodList", saveFoodList)
        context.startActivity(intent)
    }

    fun navigateSetting(context: Context) {
        val intent = Intent().setClassName(
            context.packageName,
            NavigationConstants.SETTING_ACTIVITY
        )
        context.startActivity(intent)
    }

    fun navigateKakaoMap(context: Context, latitude: Double, longitude: Double) {
        val intent = Intent().setClassName(
            context.packageName,
            NavigationConstants.KAKAO_MAP_ACTIVITY
        )
        intent.putExtra("latitude", latitude)
        intent.putExtra("longitude", longitude)
        context.startActivity(intent)
    }
}