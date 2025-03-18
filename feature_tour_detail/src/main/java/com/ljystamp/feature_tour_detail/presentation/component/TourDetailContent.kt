package com.ljystamp.feature_tour_detail.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ljystamp.core_ui.theme.AppTypography
import com.ljystamp.utils.removeHtmlTags

@Composable
fun TourDetailContent(
    title: String,
    data: String?
) {
    if(data != null && data != "") {
        Text(
            "$title ${data.removeHtmlTags()}",
            style = AppTypography.fontSize16SemiBold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        )
    }
}
