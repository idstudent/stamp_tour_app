package com.ljystamp.feature_my.presentation.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.ljystamp.core_navigation.AppRoutes
import com.ljystamp.core_ui.theme.AppColors
import com.ljystamp.core_ui.theme.AppTypography
import com.ljystamp.feature_my.R
import com.ljystamp.feature_my.domain.model.CategoryLevel
import com.ljystamp.feature_my.domain.model.LevelInfo
import com.ljystamp.feature_my.presentation.component.adventureLevel
import com.ljystamp.stamp_tour_app.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MyScreen(
    navController: NavController,
    userViewModel: UserViewModel
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val userName = userViewModel.nickname.collectAsState().value
    var completeCount = userViewModel.completeCount.collectAsState().value
    var notCompleteCount = userViewModel.notCompleteCount.collectAsState().value
    val certificationCount = userViewModel.certificationCount.collectAsState().value

    val tourPlaceList = userViewModel.tourPlaceList.collectAsState().value
    val culturePlaceList = userViewModel.cultureList.collectAsState().value
    val eventPlaceList = userViewModel.eventList.collectAsState().value
    val activityPlaceList = userViewModel.activityList.collectAsState().value
    val foodPlaceList = userViewModel.activityList.collectAsState().value

    val context = LocalContext.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                userViewModel.getUserProfileAndSavedLocations()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        userViewModel.allList.collect { allList ->
            completeCount = allList.filter { it.isVisited }.size
            notCompleteCount = allList.filter { !it.isVisited }.size
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_settings_24),
            contentDescription = null,
            tint = AppColors.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopEnd)
                .padding(top = 20.dp, end = 20.dp)
                .size(32.dp)
                .clickable { },
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_account_circle_24),
                contentDescription = null,
                tint = AppColors.White,
                modifier = Modifier.size(60.dp)
            )

            Text(
                text = userName ?: "로그인이 필요해요",
                style = AppTypography.fontSize20Regular,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 20.dp)
                    .clickable {
                        if(userName == null) {
                            navController.navigate(AppRoutes.LOGIN)
                        }
                    },
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 48.dp, horizontal = 20.dp)
                .background(
                    color = AppColors.Color2A2A2A,
                    shape = RoundedCornerShape(12.dp)
                )
                .clip(RoundedCornerShape(12.dp)), // 내용물이 모서리 넘어가지 않도록
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_tour_24),
                    contentDescription = null,
                    tint = AppColors.ColorFF8C00,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(40.dp)
                )

                Text(
                    text = completeCount.toString(),
                    style = AppTypography.fontSize20ExtraBold,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "완료",
                    style = AppTypography.fontSize16ExtraBold,
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_checklist_24),
                    contentDescription = null,
                    tint = AppColors.ColorFF8C00,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(40.dp)
                )

                Text(
                    text = notCompleteCount.toString(),
                    style = AppTypography.fontSize20ExtraBold,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "예정",
                    style = AppTypography.fontSize16ExtraBold,
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_auto_awesome_24),
                    contentDescription = null,
                    tint = AppColors.ColorFF8C00,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(40.dp)
                )

                Text(
                    text = certificationCount.toString(),
                    style = AppTypography.fontSize20ExtraBold,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "인증 획득",
                    style = AppTypography.fontSize16ExtraBold,
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
        Text(
            text = "나의 모험 레벨",
            style = AppTypography.fontSize20ExtraBold,
            modifier = Modifier.padding(start = 20.dp)
        )

        val tourVisitedCount = tourPlaceList.count { it.isVisited }
        val tourLevelInfo = calculateLevel(tourVisitedCount, CategoryLevel.TOUR)

        adventureLevel(
            title = "나의 여행지 Level",
            subTitle = CategoryLevel.TOUR.subTitleFormat.format(tourLevelInfo.targetCount),
            levelInfo = tourLevelInfo
        )

        val cultureVisitedCount = culturePlaceList.count { it.isVisited }
        val cultureLevelInfo = calculateLevel(cultureVisitedCount, CategoryLevel.CULTURE)

        adventureLevel(
            title = "나의 문화 Level",
            subTitle = CategoryLevel.CULTURE.subTitleFormat.format(cultureLevelInfo.targetCount),
            levelInfo = cultureLevelInfo
        )

        val eventVisitedCount = eventPlaceList.count { it.isVisited }
        val eventLevelInfo = calculateLevel(eventVisitedCount, CategoryLevel.EVENT)

        adventureLevel(
            title = "나의 축제 Level",
            subTitle = CategoryLevel.EVENT.subTitleFormat.format(eventLevelInfo.targetCount),
            levelInfo = eventLevelInfo
        )

        val activityVisitedCount = activityPlaceList.count { it.isVisited }
        val activityLevelInfo = calculateLevel(activityVisitedCount, CategoryLevel.ACTIVITY)

        adventureLevel(
            title = "나의 액티비티 Level",
            subTitle = CategoryLevel.ACTIVITY.subTitleFormat.format(activityLevelInfo.targetCount),
            levelInfo = activityLevelInfo
        )

        val foodVisitedCount = foodPlaceList.count { it.isVisited }
        val foodLevelInfo = calculateLevel(foodVisitedCount, CategoryLevel.FOOD)

        adventureLevel(
            title = "나의 먹방 Level",
            subTitle = CategoryLevel.FOOD.subTitleFormat.format(foodLevelInfo.targetCount),
            levelInfo = foodLevelInfo
        )

        if(userName != "로그인이 필요해요") {
            Text(
                text = "로그아웃",
                style = AppTypography.fontSize14Regular,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, end = 20.dp, bottom = 48.dp)
                    .clickable(
                        // 누를때 클릭범위 나오지 않도록
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        userViewModel.logout { success ->
                            if (success) {
                                Toast
                                    .makeText(context, "로그아웃 되었어요.", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Toast
                                    .makeText(context, "로그아웃을 실패했어요.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
            )
        }else {
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

fun calculateLevel(visitedCount: Int, category: CategoryLevel): LevelInfo {
    return when {
        visitedCount < 10 -> LevelInfo(
            level = category.beginnerLevel,
            currentCount = visitedCount,
            targetCount = 10,
            progress = (visitedCount * 100) / 10
        )

        visitedCount < 30 -> LevelInfo(
            level = category.intermediateLevel,
            currentCount = visitedCount,
            targetCount = 30,
            progress = (visitedCount * 100) / 30
        )

        else -> LevelInfo(
            level = category.advancedLevel,
            currentCount = visitedCount,
            targetCount = 50,
            progress = (visitedCount * 100) / 50
        )
    }
}
