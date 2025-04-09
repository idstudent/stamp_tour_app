package com.ljystamp.feature_my.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ljystamp.core_ui.theme.AppColors
import com.ljystamp.core_ui.theme.AppTypography
import com.ljystamp.stamp_tour_app.model.SavedLocation
import com.ljystamp.stamp_tour_app.model.TourMapper

@Composable
fun MyCompleteItem(
    savedLocation: SavedLocation
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .padding(start = 4.dp, end = 4.dp, bottom = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.Color2A2A2A
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AsyncImage(
                model = savedLocation.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(16.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = savedLocation.title,
                style = AppTypography.fontSize16SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            )

            Text(
                text = savedLocation.address,
                style = AppTypography.fontSize14SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
            )
        }
    }
}