package com.ljystamp.stamp_tour_app.navigation

import androidx.annotation.DrawableRes
import com.ljystamp.stamp_tour_app.R

sealed class NaviItem(
    val title: String?,
    @DrawableRes val iconRes: Int?,
    val route: String
) {
    companion object {
        private val BOTTOM_NVA_ROUTES = listOf("home", "search", "my")

        fun showBottomBar(route: String?): Boolean {
            return when {
                route == null -> true
                BOTTOM_NVA_ROUTES.contains(route) -> true
                else -> false
            }
        }
    }

    data object Home: NaviItem(
        title = "홈",
        iconRes = R.drawable.baseline_home_24,
        route = "home"
    )

    data object Search: NaviItem(
        title = "검색",
        iconRes = R.drawable.baseline_search_24,
        route = "search"
    )

    data object My: NaviItem(
        title = "My",
        iconRes = R.drawable.baseline_person_24,
        route = "my"
    )
}