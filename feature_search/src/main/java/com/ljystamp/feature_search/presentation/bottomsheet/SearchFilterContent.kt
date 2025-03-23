package com.ljystamp.feature_search.presentation.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ljystamp.core_ui.theme.AppColors
import com.ljystamp.core_ui.theme.AppTypography

@Composable
fun SearchFilterContent(
    onItemClick: (Int) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "필터",
                style = AppTypography.fontSize20Regular.copy(color = AppColors.Black),
                modifier = Modifier
                    .padding(top = 12.dp, start = 12.dp)
            )
        }
        FilterItem(text = "여행지", onClick = { onItemClick(12) })
        FilterItem(text = "문화", onClick = { onItemClick(14) })
        FilterItem(text = "축제", onClick = { onItemClick(15) })
        FilterItem(text = "액티비티", onClick = { onItemClick(28) })
        FilterItem(text = "음식", onClick = { onItemClick(39) })
    }
}

@Composable
private fun FilterItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        style = AppTypography.fontSize20Regular.copy(color = AppColors.Black),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(start = 12.dp, top = 12.dp, bottom = 12.dp)
    )
}