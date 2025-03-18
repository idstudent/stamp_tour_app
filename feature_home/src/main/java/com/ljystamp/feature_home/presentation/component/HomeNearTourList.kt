package com.ljystamp.feature_home.presentation.component

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.ljystamp.common.presentation.view.LoginActivity
import com.ljystamp.common.presentation.viewmodel.LocationTourListViewModel
import com.ljystamp.core_navigation.AppRoutes
import com.ljystamp.core_ui.presentation.component.NearItem
import com.ljystamp.stamp_tour_app.model.SaveResult
import com.ljystamp.stamp_tour_app.model.TourMapper
import java.net.URLEncoder

@Composable
fun HomeNearTourList(
    navController: NavController,
    nearTourList: List<TourMapper>,
    locationTourListViewModel: LocationTourListViewModel
) {
    val context = LocalContext.current
    val handleLoginRequest = {
        // TODO: 로그인화면 컴포즈로 바꾸면 수정예정
        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
    }

    if(nearTourList.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 40.dp)
        ) {
            nearTourList.take(4).forEachIndexed { index, item ->
                val isSaved = remember { mutableStateOf(false) }

                LaunchedEffect(item.contentId) {
                    locationTourListViewModel.checkIfLocationSaved(item.contentId) {
                        isSaved.value = it
                    }
                }

                NearItem(
                    nearLocation = item,
                    onButtonClick = {
                        locationTourListViewModel.saveTourLocation(item) { result ->
                            when(result) {
                                is SaveResult.Success -> {
                                    isSaved.value = true
                                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                                }
                                is SaveResult.Failure -> {
                                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                                }
                                is SaveResult.MaxLimitReached -> {
                                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                                }
                                is SaveResult.LoginRequired -> {
                                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                                    handleLoginRequest()
                                }
                            }
                        }
                    },
                    onItemClick = {
                        val gson = Gson()
                        val itemJson = gson.toJson(item)
                        val encodedItem = URLEncoder.encode(itemJson, "UTF-8")
                        navController.navigate("${AppRoutes.TOUR_DETAIL}/$encodedItem/${false}")
                    },
                    isSaved = isSaved.value
                )
                if(index < nearTourList.take(4).size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }else {
        HomeNearEmptyView()
    }
}