package com.ljystamp.feature_my.presentation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ljystamp.core_ui.theme.AppTypography
import com.ljystamp.feature_my.presentation.component.MyCompleteItem
import com.ljystamp.stamp_tour_app.model.SavedLocation

@Composable
fun MyCompleteListScreen(
    completeTourList: List<SavedLocation>?,
    completeCultureList: List<SavedLocation>?,
    completeEventList: List<SavedLocation>?,
    completeActivityList: List<SavedLocation>?,
    completeFoodList: List<SavedLocation>?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "완료한 곳",
            style = AppTypography.fontSize20ExtraBold,
            modifier = Modifier.padding(top = 20.dp, start = 20.dp)
        )

        Text(
            text = "여행지",
            style = AppTypography.fontSize20ExtraBold,
            modifier = Modifier.padding(top = 20.dp, start = 20.dp, bottom = 12.dp)
        )

        completeTourList?.let { item ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                userScrollEnabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(((item.size + 1) / 2 * 320).dp)
            ){
                items(item.size) {
                    MyCompleteItem(item[it])
                }
            }
        }

        Text(
            text = "문화",
            style = AppTypography.fontSize20ExtraBold,
            modifier = Modifier.padding(top = 20.dp, start = 20.dp, bottom = 12.dp)
        )

        completeCultureList?.let { item ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                userScrollEnabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(((item.size + 1) / 2 * 320).dp)
            ){
                items(item.size) {
                    MyCompleteItem(item[it])
                }
            }
        }

        Text(
            text = "축제",
            style = AppTypography.fontSize20ExtraBold,
            modifier = Modifier.padding(top = 20.dp, start = 20.dp, bottom = 12.dp)
        )

        completeEventList?.let { item ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                userScrollEnabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(((item.size + 1) / 2 * 320).dp)
            ){
                items(item.size) {
                    MyCompleteItem(item[it])
                }
            }
        }

        Text(
            text = "액티비티",
            style = AppTypography.fontSize20ExtraBold,
            modifier = Modifier.padding(top = 20.dp, start = 20.dp, bottom = 12.dp)
        )

        completeActivityList?.let { item ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                userScrollEnabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(((item.size + 1) / 2 * 320).dp)
            ){
                items(item.size) {
                    MyCompleteItem(item[it])
                }
            }
        }

        Text(
            text = "음식",
            style = AppTypography.fontSize20ExtraBold,
            modifier = Modifier.padding(top = 20.dp, start = 20.dp, bottom = 12.dp)
        )

        completeFoodList?.let { item ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                userScrollEnabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(((item.size + 1) / 2 * 320).dp)
            ){
                items(item.size) {
                    MyCompleteItem(item[it])
                }
            }
        }

    }
}