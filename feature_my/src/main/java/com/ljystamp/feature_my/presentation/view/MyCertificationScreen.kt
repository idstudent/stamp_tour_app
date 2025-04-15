package com.ljystamp.feature_my.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ljystamp.core_ui.theme.AppTypography
import com.ljystamp.feature_my.R
import com.ljystamp.feature_my.domain.model.CategoryLevel
import com.ljystamp.stamp_tour_app.model.SavedLocation

@Composable
fun MyCertificationScreen(
    completeTourList: List<SavedLocation>?,
    completeCultureList: List<SavedLocation>?,
    completeEventList: List<SavedLocation>?,
    completeActivityList: List<SavedLocation>?,
    completeFoodList: List<SavedLocation>?
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ){
        Text(
            text = "획득한 뱃지",
            style = AppTypography.fontSize20ExtraBold,
            modifier = Modifier.padding(top = 20.dp, start = 20.dp, bottom = 20.dp)
        )

        BadgeRow(
            title = "여행지",
            itemList = completeTourList ?: arrayListOf(),
            badge1 = Pair(R.drawable.tour_img_1, "여행\n병아리"),
            badge2 = Pair(R.drawable.tour_img_2, "방랑"),
            badge3 = Pair(R.drawable.tour_img_3, "콜럼버스")
        )

        BadgeRow(
            title = "문화",
            itemList = completeCultureList ?: arrayListOf(),
            badge1 = Pair(R.drawable.culture_img_1, "관람"),
            badge2 = Pair(R.drawable.culture_img_2, "예술인"),
            badge3 = Pair(R.drawable.culture_img_3, "Art\n그 자체")
        )

        BadgeRow(
            title = "축제",
            itemList = completeEventList ?: arrayListOf(),
            badge1 = Pair(R.drawable.festival_img_1, "가볍게\n즐기는 사람"),
            badge2 = Pair(R.drawable.festival_img_2, "축제에\n미쳐버린 사람"),
            badge3 = Pair(R.drawable.festival_img_3, "내가\n바로 MC")
        )

        BadgeRow(
            title = "액티비티",
            itemList = completeActivityList ?: arrayListOf(),
            badge1 = Pair(R.drawable.activity_img_1, "뉴비"),
            badge2 = Pair(R.drawable.activity_img_2, "고인물"),
            badge3 = Pair(R.drawable.activity_img_3, "통달한\n사람")
        )

        BadgeRow(
            title = "음식",
            itemList = completeFoodList ?: arrayListOf(),
            badge1 = Pair(R.drawable.food_img_1, "맛집 찾아\n헤매는 뉴비"),
            badge2 = Pair(R.drawable.food_img_2, "고독한\n미식가"),
            badge3 = Pair(R.drawable.food_img_3, "먹방 신")
        )
    }
}

@Composable
fun BadgeRow(
    title: String,
    itemList: List<SavedLocation>,
    badge1: Pair<Int, String>,
    badge2: Pair<Int, String>,
    badge3: Pair<Int, String>
) {
    Column {
        Text(
            text = title,
            style = AppTypography.fontSize20ExtraBold,
            modifier = Modifier.padding(start = 20.dp)
        )

        val visitedCount = itemList.filter { it.isVisited }.size

        if(visitedCount < 10) return

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            StampItem(image = badge1.first, title = badge1.second)

            when (visitedCount) {
                in 30..49 -> StampItem(image = badge2.first, title = badge2.second)
                in 50..Int.MAX_VALUE -> {
                    StampItem(image = badge2.first, title = badge2.second)
                    StampItem(image = badge3.first, title = badge3.second)
                }
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
fun StampItem(
    image: Int,
    title: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier.size(60.dp)
        )

        Text(
            text = title,
            style = AppTypography.fontSize16Regular,
            textAlign = TextAlign.Center
        )
    }
}