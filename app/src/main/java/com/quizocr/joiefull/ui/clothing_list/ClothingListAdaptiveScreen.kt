package com.quizocr.joiefull.ui.clothing_list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.quizocr.joiefull.ui.clothing_list.components.ClothingGrid
import com.quizocr.joiefull.ui.clothing_list.components.MasterDetailLayout
import com.quizocr.joiefull.ui.util.calculateGridColumns

/**
 * Adaptive screen that renders different layouts based on window size
 * - Compact/Medium: Phone layout with 2 columns and navigation
 * - Expanded: Tablet layout with master-detail and 3-4 columns
 */
@Composable
fun ClothingListAdaptiveScreen(
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    viewModel: ClothingListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val isExpanded = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded

    // Selection state for tablet master-detail
    var selectedItemId by rememberSaveable { mutableStateOf<Int?>(null) }

    // Auto-select first item on tablet launch
    LaunchedEffect(state.clothingItems.size, isExpanded, state.isLoading) {
        if (isExpanded && !state.isLoading && selectedItemId == null && state.clothingItems.isNotEmpty()) {
            selectedItemId = state.clothingItems.first().id
        }
    }

    val columns = calculateGridColumns(windowSizeClass)

    if (isExpanded) {
        // Tablet: Master-detail layout
        MasterDetailLayout(
            listState = state,
            columns = columns,
            selectedItemId = selectedItemId,
            onItemSelected = { itemId -> selectedItemId = itemId },
            onFavoriteClick = { itemId -> viewModel.onFavoriteClicked(itemId) },
            modifier = Modifier.fillMaxSize()
        )
    } else {
        // Phone: Traditional grid with navigation
        ClothingGrid(
            state = state,
            columns = 2,
            onItemClick = { itemId ->
                navController.navigate("clothing_detail/$itemId")
            },
            onFavoriteClick = { itemId -> viewModel.onFavoriteClicked(itemId) }
        )
    }
}
