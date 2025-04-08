package com.ljystamp.feature_my.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ljystamp.core_ui.theme.AppColors
import com.ljystamp.core_ui.theme.AppTypography
import com.ljystamp.feature_my.domain.model.LevelInfo

@Composable
fun adventureLevel(
    title: String,
    subTitle: String,
    levelInfo: LevelInfo
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 20.dp, end = 20.dp)
            .background(
                color = AppColors.Color2A2A2A,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
    ) {
        Text(
            text = title,
            style = AppTypography.fontSize20ExtraBold,
            modifier = Modifier.padding(top = 20.dp, start = 20.dp)
        )

        Text(
            text = subTitle,
            style = AppTypography.fontSize16SemiBold,
            modifier = Modifier.padding(top = 8.dp, start = 20.dp)
        )

        Row(
           modifier = Modifier
               .fillMaxWidth()
               .padding(top = 20.dp, start = 20.dp, end = 20.dp),
        ) {
            Text(
                text = levelInfo.level,
                style = AppTypography.fontSize16Regular,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = levelInfo.currentCount.toString(),
                style = AppTypography.fontSize16Regular
            )
            Text(
                text = " / ${levelInfo.targetCount}",
                style = AppTypography.fontSize16Regular
            )
        }

        CustomProgressBar(progress = levelInfo.progress)
    }
}

@Composable
fun CustomProgressBar(
    progress: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 20.dp)
            .height(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(AppColors.Color3D3D3D)
    ) {
        val safeProgress = if (progress > 100) 100 else progress
        val progressFraction = safeProgress.toFloat() / 100f

        Box(
            modifier = Modifier
                .fillMaxWidth(progressFraction)
                .height(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(AppColors.ColorFF8C00)
        )
    }
}