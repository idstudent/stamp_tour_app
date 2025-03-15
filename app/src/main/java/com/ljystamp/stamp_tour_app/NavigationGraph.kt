package com.ljystamp.stamp_tour_app

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ljystamp.core_navigation.AppRoutes
import com.ljystamp.core_navigation.NaviItem
import com.ljystamp.feature_home.presentation.view.HomeScreen
import com.ljystamp.feature_near_place.presentation.view.NearPlaceListScreen
@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NaviItem.Home.route,
    ) {
        composable(route = NaviItem.Home.route) {
            HomeScreen(navController)
        }
        composable(route = NaviItem.Search.route) {
            HomeScreen(navController)
        }
        composable(route = NaviItem.My.route) {
            HomeScreen(navController)
        }

        composable(
            route = "${AppRoutes.NEAR_PLACE_LIST}/{contentTypeId}",
            arguments = listOf(
                navArgument("contentTypeId") { type = NavType.IntType }
            )
        ) {
            val contentTypeId = it.arguments?.getInt("contentTypeId") ?: 0
            NearPlaceListScreen(
                locationTourListViewModel = hiltViewModel(),
                contentTypeId = contentTypeId
            )
        }
    }
}
