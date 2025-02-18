package com.ljystamp.core_navigation

import android.content.Context
import android.content.Intent
import android.os.Parcelable

object NavigationConstants {
    const val TOUR_DETAIL_ACTIVITY = "com.ljystamp.feature_tour_detail.presentation.TourDetailActivity"
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
}