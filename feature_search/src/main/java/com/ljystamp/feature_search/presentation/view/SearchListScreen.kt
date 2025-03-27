package com.ljystamp.feature_search.presentation.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.google.gson.Gson
import com.ljystamp.common.presentation.viewmodel.LocationTourListViewModel
import com.ljystamp.core_navigation.AppRoutes
import com.ljystamp.core_ui.presentation.component.TourItem
import com.ljystamp.core_ui.theme.AppTypography
import com.ljystamp.feature_search.presentation.viewmodel.SearchKeywordViewModel
import com.ljystamp.stamp_tour_app.model.SaveResult
import com.ljystamp.stamp_tour_app.model.TourMapper
import kotlinx.coroutines.launch
import java.net.URLEncoder

@Composable
fun SearchListScreen(
    navController: NavController,
    contentTypeId: Int,
    keyword: String,
    searchKeywordViewModel: SearchKeywordViewModel,
    locationTourListViewModel: LocationTourListViewModel
) {
    var page by remember { mutableStateOf(1) }
    var isLoading by remember { mutableStateOf(false) }

    val currentResultList = remember { mutableStateListOf<TourMapper>() }

    val context = LocalContext.current

    fun search() {
        if (isLoading) return
        isLoading = true

        searchKeywordViewModel.getSearchKeywordResult(keyword, contentTypeId, page)
    }

    LaunchedEffect(contentTypeId, page) {
        search()
    }

    LaunchedEffect(Unit) {
        searchKeywordViewModel.getSearchKeyWordResult.collect { newTourList ->
            if(page == 1) {
                currentResultList.clear()
            }

            if(newTourList.isEmpty()) {
                isLoading = true
            } else {
                currentResultList.addAll(newTourList)
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "검색 결과",
            style = AppTypography.fontSize20ExtraBold,
            modifier = Modifier.padding(top = 32.dp, start = 20.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp, start = 16.dp, end = 16.dp)
        ) {
            items(currentResultList.size) { index ->
                val item = currentResultList[index]
                val isSaved = remember { mutableStateOf(false) }

                LaunchedEffect(item.contentId) {
                    locationTourListViewModel.checkIfLocationSaved(item.contentId) {
                        isSaved.value = it
                    }
                }

                TourItem(
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
                                    navController.navigate(AppRoutes.LOGIN)
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
                if (index >= currentResultList.size - 3 && !isLoading) {
                    page++
                    search()
                }
            }
        }
    }
}