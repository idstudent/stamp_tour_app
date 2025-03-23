package com.ljystamp.feature_search.presentation.bottomsheet

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import com.ljystamp.core_ui.theme.AppColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onFilterSelected: (Int) -> Unit
) {
    if(isVisible) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val scope = rememberCoroutineScope()

        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = AppColors.White,
            scrimColor = AppColors.Black.copy(alpha = 0.5f), // 바텀시트 뜰때 뒤에 배경(알파 지정안하면 뒤에 컨텐츠 안보임)
            dragHandle = {}
        ) {
            SearchFilterContent(
                onItemClick = {
                    onFilterSelected(it)
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if(!sheetState.isVisible) onDismiss()
                    }
                }
            )
        }
    }
}