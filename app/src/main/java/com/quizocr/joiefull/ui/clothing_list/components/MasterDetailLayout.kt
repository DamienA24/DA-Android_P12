package com.quizocr.joiefull.ui.clothing_list.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.quizocr.joiefull.ui.clothing_detail.ClothingDetailContent
import com.quizocr.joiefull.ui.clothing_list.ClothingListState

@Composable
fun MasterDetailLayout(
    listState: ClothingListState,
    columns: Int,
    selectedItemId: Int?,
    onItemSelected: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxSize()) {
        ClothingGrid(
            state = listState,
            columns = columns,
            onItemClick = onItemSelected,
            onFavoriteClick = onFavoriteClick,
            modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight()
        )

        Spacer(modifier = Modifier.width(16.dp))

        Box(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight()
        ) {
            selectedItemId?.let { id ->
                ClothingDetailContent(
                    clothingId = id,
                    showBackButton = false,
                    onBackClick = null
                )
            } ?: EmptyDetailPlaceholder()
        }
    }
}
