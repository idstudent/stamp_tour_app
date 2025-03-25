package com.ljystamp.feature_home.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ljystamp.core_ui.theme.AppColors
import com.ljystamp.core_ui.theme.AppTypography
import com.ljystamp.stamp_tour_app.model.SavedLocation

@Composable
fun StampViewPagerItem (
    savedLocation: SavedLocation,
    onClick: () -> Unit,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clickable(onClick = onItemClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.Color2A2A2A
        )
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
        ) {
            AsyncImage(
                model = savedLocation.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp)
                ) {
                    Text(
                        text = savedLocation.title,
                        style = AppTypography.fontSize16SemiBold,
                        maxLines = 1
                    )
                    Text(
                        text = savedLocation.address,
                        style = AppTypography.fontSize14SemiBold,
                        maxLines = 2
                    )
                }

                Button(
                    onClick = onClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.ColorFF8C00
                    )
                ) {
                    Text(
                        text = "스탬프 찍기",
                        style = AppTypography.fontSize16SemiBold,
                    )
                }
            }
        }
    }
}