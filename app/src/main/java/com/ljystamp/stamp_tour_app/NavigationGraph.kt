package com.ljystamp.stamp_tour_app

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.ljystamp.common.presentation.view.LoginScreen
import com.ljystamp.core_navigation.AppRoutes
import com.ljystamp.core_navigation.NaviItem
import com.ljystamp.feature_home.presentation.view.HomeScreen
import com.ljystamp.feature_my.presentation.view.MyCompleteListScreen
import com.ljystamp.feature_my.presentation.view.MyScreen
import com.ljystamp.feature_my.presentation.view.SettingScreen
import com.ljystamp.feature_my_tour.presentation.view.MyTourListScreen
import com.ljystamp.feature_my_tour_detail.presentation.view.MyTourDetailScreen
import com.ljystamp.feature_near_place.presentation.view.NearPlaceListScreen
import com.ljystamp.feature_search.presentation.view.SearchListScreen
import com.ljystamp.feature_search.presentation.view.SearchScreen
import com.ljystamp.feature_tour_detail.presentation.view.TourDetailScreen
import com.ljystamp.stamp_tour_app.model.SavedLocation
import com.ljystamp.stamp_tour_app.model.TourMapper
import java.net.URLDecoder
import kotlin.Exception

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
            SearchScreen(navController)
        }
        composable(route = NaviItem.My.route) {
            MyScreen(
                navController,
                userViewModel = hiltViewModel()
            )
        }

        composable(route = "${AppRoutes.LOGIN}") {
            LoginScreen(
                navController = navController,
                loginViewModel = hiltViewModel()
            )
        }

        composable(
            route = "${AppRoutes.NEAR_PLACE_LIST}/{contentTypeId}",
            arguments = listOf(
                navArgument("contentTypeId") { type = NavType.IntType }
            )
        ) {
            val contentTypeId = it.arguments?.getInt("contentTypeId") ?: 0
            NearPlaceListScreen(
                navController = navController,
                locationTourListViewModel = hiltViewModel(),
                contentTypeId = contentTypeId
            )
        }

        composable(
            route = "${AppRoutes.MY_TOUR_LIST}",
        ) {
            MyTourListScreen(
                navController = navController,
                locationTourListViewModel = hiltViewModel(),
            )
        }

        composable(
            route = "${AppRoutes.TOUR_DETAIL}/{info}/{search}",
            arguments = listOf(
                navArgument("info") { type = NavType.StringType },
                navArgument("search") { type = NavType.BoolType}
            )
        ) {
            val info = it.arguments?.getString("info") ?: ""
            val search = it.arguments?.getBoolean("search") ?: false

            val decodedJson = URLDecoder.decode(info, "UTF-8")

            val tourDetailInfo = try {
                Gson().fromJson(decodedJson, TourMapper::class.java)
            } catch (e: Exception) {
                null
            }

            TourDetailScreen(
                navController = navController,
                tourDetailViewModel = hiltViewModel(),
                locationTourListViewModel = hiltViewModel(),
                tourInfo = tourDetailInfo,
                enterSearch = search
            )
        }

        composable(
            route = "${AppRoutes.MY_TOUR_DETAIL}/{info}",
            arguments = listOf(
                navArgument("info") { type = NavType.StringType}
            )
        ) {
            val info = it.arguments?.getString("info") ?: ""

            val decodedJson = URLDecoder.decode(info, "UTF-8")

            val tourDetailInfo = try {
                Gson().fromJson(decodedJson, SavedLocation::class.java)
            }catch (e: Exception) {
                null
            }

            MyTourDetailScreen(
                navController = navController,
                tourDetailViewModel = hiltViewModel(),
                locationTourListViewModel = hiltViewModel(),
                tourInfo = tourDetailInfo
            )
        }

        composable(
            route = "${AppRoutes.SEARCH_LIST}/{contentTypeId}/{keyword}",
            arguments = listOf(
                navArgument("contentTypeId") { type = NavType.IntType},
                navArgument("keyword") { type = NavType.StringType}
            )
        ) {
            val contentTypeId = it.arguments?.getInt("contentTypeId") ?: 0
            val keyword = it.arguments?.getString("keyword") ?: ""


            SearchListScreen(
                navController = navController,
                contentTypeId = contentTypeId,
                keyword = keyword,
                searchKeywordViewModel = hiltViewModel(),
                locationTourListViewModel = hiltViewModel(),
            )
        }

        composable(
            route = "${AppRoutes.MY_COMPLETE_LIST}/{tourList}/{cultureList}/{eventList}/{activityList}/{foodList}",
            arguments = listOf(
                navArgument("tourList") { type = NavType.StringType },
                navArgument("cultureList") { type = NavType.StringType },
                navArgument("eventList") { type = NavType.StringType },
                navArgument("activityList") { type = NavType.StringType },
                navArgument("foodList") { type = NavType.StringType },
            )
        ) {
            val tourList = it.arguments?.getString("tourList") ?: ""
            val tourJson = URLDecoder.decode(tourList, "UTF-8")

            val completeTourList = try {
                Gson().fromJson(tourJson, Array<SavedLocation>::class.java)?.toList()
            } catch (e: Exception) {
                null
            }

            val cultureList = it.arguments?.getString("cultureList") ?: ""
            val cultureJson = URLDecoder.decode(cultureList, "UTF-8")

            val completeCultureList = try {
                Gson().fromJson(cultureJson, Array<SavedLocation>::class.java)?.toList()
            } catch (e: Exception) {
                null
            }

            val eventList = it.arguments?.getString("eventList") ?: ""
            val eventJson = URLDecoder.decode(eventList, "UTF-8")

            val completeEventList = try {
                Gson().fromJson(eventJson, Array<SavedLocation>::class.java)?.toList()
            }catch (e: Exception) {
                null
            }

            val activityList = it.arguments?.getString("activityList") ?: ""
            val activityJson = URLDecoder.decode(activityList, "UTF-8")

            val completeActivityList = try {
                Gson().fromJson(activityJson, Array<SavedLocation>::class.java)?.toList()
            }catch (e: Exception) {
                null
            }

            val foodList = it.arguments?.getString("foodList") ?: ""
            val foodJson = URLDecoder.decode(foodList, "UTF-8")

            val completeFoodList = try {
                Gson().fromJson(foodJson, Array<SavedLocation>::class.java)?.toList()
            }catch (e: Exception) {
                null
            }

            MyCompleteListScreen(
                completeTourList,
                completeCultureList,
                completeEventList,
                completeActivityList,
                completeFoodList
            )
        }

        composable(
            route = AppRoutes.SETTING,
        ) {
            SettingScreen(navController = navController)
        }
    }
}
