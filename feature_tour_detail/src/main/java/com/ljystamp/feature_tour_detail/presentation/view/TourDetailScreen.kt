package com.ljystamp.feature_tour_detail.presentation.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ljystamp.common.presentation.viewmodel.LocationTourListViewModel
import com.ljystamp.core_ui.theme.AppColors
import com.ljystamp.core_ui.theme.AppTypography
import com.ljystamp.feature_tour_detail.presentation.component.TourDetailContent
import com.ljystamp.feature_tour_detail.presentation.viewmodel.TourDetailViewModel
import com.ljystamp.stamp_tour_app.model.SaveResult
import com.ljystamp.stamp_tour_app.model.TourMapper

@Composable
fun TourDetailScreen(
    navController: NavController,
    tourDetailViewModel: TourDetailViewModel,
    locationTourListViewModel: LocationTourListViewModel,
    tourInfo: TourMapper?,
    enterSearch: Boolean
) {
    val detailInfo = tourDetailViewModel.tourDetailInfo.collectAsState().value
    val isSaved = remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        tourInfo?.let {
            tourDetailViewModel.getTourDetail(tourInfo.contentId, tourInfo.contentTypeId)

            if (enterSearch) {
                tourDetailViewModel.insertItem(it)
            }

            locationTourListViewModel.checkIfLocationSaved(it.contentId) { saved ->
                isSaved.value = saved
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 88.dp)
                .verticalScroll(rememberScrollState())
        ) {
            tourInfo?.let {
                AsyncImage(
                    model = it.firstImage ?: "",
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = it.title ?: "",
                    style = AppTypography.fontSize24ExtraBold,
                    modifier = Modifier
                        .padding(top = 20.dp, start = 16.dp, end = 16.dp)
                )

                Text(
                    text = it.addr1 ?: "",
                    style = AppTypography.fontSize16SemiBold,
                    modifier = Modifier
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                )

                if (detailInfo.isNotEmpty()) {
                    when (it.contentTypeId) {
                        12 -> {
                            TourDetailContent("개장일:", detailInfo[0].openDate)
                            TourDetailContent("휴무일:", detailInfo[0].restDate)
                            TourDetailContent("이용 가능 시간:", detailInfo[0].useTime)
                        }

                        14 -> {
                            TourDetailContent("휴무일:", detailInfo[0].cultureRestDate)
                            TourDetailContent("이용 가능 시간:", detailInfo[0].cultureUseTime)
                            TourDetailContent("이용 요금:", detailInfo[0].culturePrice)
                            TourDetailContent("주차:", detailInfo[0].cultureParking)
                            TourDetailContent("주차 요금:", detailInfo[0].cultureParkingFee)
                            TourDetailContent("문의:", detailInfo[0].cultureInfoCenter)
                        }

                        15 -> {
                            TourDetailContent("행사 시작일:", detailInfo[0].eventStartDate)
                            TourDetailContent("행사 종료일:", detailInfo[0].eventEndDate)
                            TourDetailContent("행사 시간:", detailInfo[0].eventPlayTime)
                            TourDetailContent("장소:", detailInfo[0].eventPlace)
                            TourDetailContent("이용 금액:", detailInfo[0].eventUsePrice)
                            TourDetailContent("주최자:", detailInfo[0].eventSponsor)
                            TourDetailContent("주최자 문의:", detailInfo[0].eventSponsorTel)
                        }

                        28 -> {
                            TourDetailContent("휴무일:", detailInfo[0].activityRestDate)
                            TourDetailContent("이용 가능 시간:", detailInfo[0].activityUseTime)
                            TourDetailContent("이용 가능 연령:", detailInfo[0].activityPossibleAge)
                            TourDetailContent("주차 및 요금:", detailInfo[0].activityParking)
                            TourDetailContent("예약 안내:", detailInfo[0].activityReservation)
                            TourDetailContent("문의:", detailInfo[0].activityInfoCenter)
                        }

                        39 -> {
                            TourDetailContent("휴무일:", detailInfo[0].foodRestTime)
                            TourDetailContent("영업 시간:", detailInfo[0].foodOpenTime)
                            TourDetailContent("대표 메뉴:", detailInfo[0].foodFirstMenu)
                            TourDetailContent("메뉴:", detailInfo[0].foodTreatMenu)
                            TourDetailContent("포장:", detailInfo[0].foodTakeOut)
                            TourDetailContent("문의:", detailInfo[0].foodInfoCenter)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                .height(48.dp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (isSaved.value) AppColors.Color3D3D3D else AppColors.ColorFF8C00
                )
                .clickable {
                    tourInfo?.let {
                        locationTourListViewModel.saveTourLocation(it) { result ->
                            when (result) {
                                is SaveResult.Success -> {
                                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                }
                                is SaveResult.Failure -> {
                                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                                }
                                is SaveResult.MaxLimitReached -> {
                                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                                }
                                is SaveResult.LoginRequired -> {
                                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                                    // handleLoginRequest()
                                }
                            }
                        }
                    }
                }
        ) {
            Text(
                text = "등록",
                style = AppTypography.fontSize20SemiBold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}