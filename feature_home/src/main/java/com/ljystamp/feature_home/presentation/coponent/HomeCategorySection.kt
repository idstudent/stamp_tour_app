package com.ljystamp.feature_home.presentation.coponent

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ljystamp.feature_home.R

@Composable
fun HomeCategorySection(context: Context) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        HomeSubjectButton(
            icon = R.drawable.baseline_synagogue_24,
            title = "문화",
            onClick = {
//                Navigator.navigateToNearPlaceList(context, 14)
            }
        )

        HomeSubjectButton(
            icon = R.drawable.baseline_celebration_24,
            title = "축제",
            onClick = {
//                Navigator.navigateToNearPlaceList(context, 15)
            }
        )

        HomeSubjectButton(
            icon = R.drawable.baseline_directions_run_24,
            title = "액티비티",
            onClick = {
//                Navigator.navigateToNearPlaceList(context, 28)
            }
        )

        HomeSubjectButton(
            icon = R.drawable.baseline_food_bank_24,
            title = "음식",
            onClick = {
//                Navigator.navigateToNearPlaceList(context, 39)
            }
        )
    }
}