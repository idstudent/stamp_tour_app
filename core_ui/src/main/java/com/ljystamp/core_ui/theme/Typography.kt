package com.ljystamp.core_ui.theme

import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.ljystamp.core_ui.R


private val NotoSansRegular = FontFamily(Font(R.font.noto_regular))
private val NotoSansExtraBold = FontFamily(Font(R.font.noto_extra_bold))
private val NotoSansSemiBold = FontFamily(Font(R.font.noto_semi_bold))

object AppTypography {
    val fontSize14Regular = TextStyle(
        fontFamily = NotoSansRegular,
        fontSize = 14.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
        color = AppColors.White
    )

    val fontSize16Regular = TextStyle(
        fontFamily = NotoSansRegular,
        fontSize = 16.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
        color = AppColors.White
    )

    val fontSize20Regular = TextStyle(
        fontFamily = NotoSansRegular,
        fontSize = 20.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
        color = AppColors.White
    )


    val fontSize14SemiBold = TextStyle(
        fontFamily = NotoSansSemiBold,
        fontSize = 14.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
        color = AppColors.White
    )

    val fontSize16SemiBold = TextStyle(
        fontFamily = NotoSansSemiBold,
        fontSize = 16.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
        color = AppColors.White
    )

    val fontSize20SemiBold = TextStyle(
        fontFamily = NotoSansSemiBold,
        fontSize = 20.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
        color = AppColors.White
    )

    val fontSize20ExtraBold = TextStyle(
        fontFamily = NotoSansExtraBold,
        fontSize = 20.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
        color = AppColors.White
    )

    val fontSize24ExtraBold = TextStyle(
        fontFamily = NotoSansExtraBold,
        fontSize = 24.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
        color = AppColors.White
    )
}